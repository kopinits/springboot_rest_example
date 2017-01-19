package com.markus.app.controller;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.markus.app.exception.BadRequestException;
import com.markus.app.model.PagingBO;
import com.markus.app.model.SortDirection;
import com.markus.app.model.filter.Filter;
import com.markus.app.model.filter.FilterCriteria;
import com.markus.app.model.filter.QueryOption;
import com.markus.app.support.ToFilterCriteriaConverter;

public class AbstractController<A extends Filter<?>> {

	protected static final class QueryParam {
		private String name;
		private final Class<?> paramClass;
		private ToFilterCriteriaConverter<?, ?> filterCriteriaConverter;
		private boolean option = false;
		private Class<?> typeClass;

		public <A> QueryParam(final String name, final Class<?> paramClass,
				final ToFilterCriteriaConverter<A, ?> filterCriteriaConverter) {
			this.name = name;
			this.paramClass = paramClass;
			this.filterCriteriaConverter = filterCriteriaConverter;
		}

		public <A> QueryParam(final String name, final Class<?> paramClass) {
			this(name, paramClass, null);
		}

		public <A> QueryParam(final String name) {
			this(name, String.class, null);
		}

		public String getName() {
			return name;
		}

		public Class<?> getParamClass() {
			return paramClass;
		}

		public ToFilterCriteriaConverter<?, ?> getFilterCriteriaConverter() {
			return filterCriteriaConverter;
		}

		public boolean isOption() {
			return option;
		}

		public Class<?> getTypeClass() {
			return typeClass;
		}

		public QueryParam withOption() {
			this.option = true;
			return this;
		}
	}

	private static final String ORDER_BY_PARAM = "orderBy";
	private static final String ASCENDING_PARAM = "order";
	private static final String SIZE_PARAM = "size";
	private static final String PAGE_PARAM = "page";
	private static final String DESCENDING_ORDER = "desc";

	private static final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<Class<?>, Function<String, Object>>();

	static {
		CONVERTERS.put(Integer.class, value -> Integer.parseInt(value));
		CONVERTERS.put(Long.class, value -> Long.parseLong(value));
		CONVERTERS.put(DateTime.class, value -> ISODateTimeFormat.dateTimeParser().withZoneUTC().parseDateTime(value));
		CONVERTERS.put(BigDecimal.class, value -> new BigDecimal(value));
		CONVERTERS.put(Boolean.class, value -> {
			final String upper = value.toUpperCase();
			if ("TRUE".equals(upper)) {
				return Boolean.TRUE;
			} else if ("FALSE".equals(upper)) {
				return Boolean.FALSE;
			} else {
				throw new IllegalArgumentException("Not a valid boolean = {true, false}");
			}
		});
	}

	private final Class<A> filterClass;
	private final Map<String, Method> setters;
	private final Map<String, QueryParam> filterParameters;

	protected AbstractController(final Class<A> filterClass, final QueryParam... params) {
		this.filterClass = filterClass;
		this.filterParameters = new HashMap<String, QueryParam>(params.length);
		this.setters = new HashMap<String, Method>(params.length);

		for (QueryParam param : params) {
			final String setter = "set" + Character.toUpperCase(param.getName().charAt(0))
					+ param.getName().substring(1);
			final Class<?>[] setterParameters = determineFilterSetterParameterClasses(param);

			try {
				setters.put(param.getName(), filterClass.getMethod(setter, setterParameters));
				filterParameters.put(param.getName(), param);
			} catch (Exception e) {
				throw new IllegalStateException("Filter " + filterClass.getSimpleName()
						+ " is required to have a setter " + setter + "(" + param.getParamClass().getSimpleName()
						+ (param.isOption() ? ", QueryOption" : "") + ") for parameter " + param.getName(), e);
			}

		}
	}

	private static Class<?>[] determineFilterSetterParameterClasses(final QueryParam param) {
		if (param.getFilterCriteriaConverter() != null) {
			// Converter to filter criteria is present: there will be a setter
			// for the converted value (=FilterCriteria)
			// Options are not relevant here: they are handled by the converter
			return new Class<?>[] { FilterCriteria.class };
		} else if (param.isOption()) {
			// Option is present: there will be setter for the value and option
			return new Class<?>[] { param.getParamClass(), QueryOption.class };
		} else {
			return new Class<?>[] { param.getParamClass() };
		}
	}

	protected PagingBO createPagingConfiguration(final Map<String, String> params) {
		return createPagingConfiguration(params, filterParameters);
	}

	private PagingBO createPagingConfiguration(final Map<String, String> params,
			final Map<String, QueryParam> allowedParameters) {
		Integer page = convert(Integer.class, PAGE_PARAM, params.get(PAGE_PARAM));
		Integer size = convert(Integer.class, SIZE_PARAM, params.get(SIZE_PARAM));
		String asc = convert(String.class, ASCENDING_PARAM, params.get(ASCENDING_PARAM));
		String orderBy = convert(String.class, ORDER_BY_PARAM, params.get(ORDER_BY_PARAM));
		if (orderBy != null && ("".equals(orderBy) || !allowedParameters.containsKey(orderBy))) {
			orderBy = null;
		}
		return new PagingBO(page, size, SortDirection.isAsc(defineAscOrdering(asc)), orderBy);
	}

	private boolean defineAscOrdering(String asc) {
		boolean isAsc = true;
		if (StringUtils.isNotEmpty(asc)) {
			if (asc.toLowerCase().equals(DESCENDING_ORDER)) {
				isAsc = false;
			}
		}
		return isAsc;
	}

	protected A createFilter(final Map<String, String> params) {
		return createFilter(filterClass, params, filterParameters, setters);
	}

	@SuppressWarnings("unchecked")
	private <X> X createFilter(final Class<X> clazz, final Map<String, String> params,
			final Map<String, QueryParam> allowedParameters, final Map<String, Method> allowedMethodMap) {
		X filter;
		try {
			filter = clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Exception while instantiating class " + clazz.getSimpleName(), e);
		}

		for (QueryParam qp : allowedParameters.values()) {
			final String value = params.get(qp.getName());
			if (value != null) {
				Object convertedValue = convert(qp.getParamClass(), qp.getTypeClass(), qp.getName(), value);
				QueryOption convertedOption = null;
				if (qp.isOption()) {
					final String optionParam = qp.getName() + "Option";
					convertedOption = convert(QueryOption.class, optionParam, params.get(optionParam));
				}

				@SuppressWarnings("rawtypes")
				// Note: through convert(qp.getParamClass()) and through the
				// binding of the filter criteria converter in the QueryParam
				// constructor,
				// the following is using the correct types.
				ToFilterCriteriaConverter converter = qp.getFilterCriteriaConverter();
				if (converter != null) {
					convertedValue = converter.convertToFilterCriteria(convertedValue, convertedOption);
				}

				try {
					if (qp.isOption() && converter == null) {
						// only condition in which we have an option argument.
						// If there is a converter, the option is handled
						// there...
						allowedMethodMap.get(qp.getName()).invoke(filter, convertedValue, convertedOption);
					} else {
						allowedMethodMap.get(qp.getName()).invoke(filter, convertedValue);
					}
				} catch (Exception e) {
					throw new IllegalStateException("Unable to invoke setter of " + qp.getName() + " on "
							+ clazz.getSimpleName() + " with value (" + convertedValue
							+ (qp.isOption() ? ", " + convertedOption : "") + ")", e);
				}
			}
		}
		return filter;

	}

	private <T> T convertEnum(final Class<T> targetClass, final String paramName, final String value) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Enum<?> enumValue = EnumUtils.getEnum((Class<Enum>) targetClass, value);
		final T result = targetClass.cast(enumValue);
		if (result == null) {
			throw new BadRequestException("Value '" + value + "' is not valid for " + paramName + ", must be a "
					+ targetClass.getSimpleName());
		} else {
			return result;
		}
	}

	protected <T> T convert(final Class<T> targetClass, final String paramName, final String value) {
		return convert(targetClass, null, paramName, value);
	}

	@SuppressWarnings("unchecked")
	protected <T, S> T convert(final Class<T> targetClass, final Class<S> subTypeClass, final String paramName,
			final String value) {
		try {

			if (value == null) {
				return null;
			} else if (targetClass.isEnum()) {
				return convertEnum(targetClass, paramName, value);
			} else if (targetClass.isAssignableFrom(List.class)) {
				List<S> returnList = new ArrayList<S>();
				for (String valueElem : Arrays.asList(value.split(","))) {
					returnList.add(convertEnum(subTypeClass, paramName, valueElem.trim()));
				}
				return (T) returnList;
			} else if (String.class.isAssignableFrom(targetClass)) {
				return targetClass.cast(value);
			} else {
				Function<String, Object> converter = CONVERTERS.get(targetClass);
				if (converter == null) {
					throw new BadRequestException("No converter found for class " + targetClass.getSimpleName()
							+ " required for " + paramName);
				}
				return targetClass.cast(converter.apply(value));
			}
		} catch (Exception e) {
			if (BadRequestException.class.isAssignableFrom(e.getClass())) {
				throw e;
			} else {
				throw new BadRequestException("Value '" + value + "' is not valid for " + paramName + ", must be a "
						+ targetClass.getSimpleName(), e);
			}
		}
	}

}
