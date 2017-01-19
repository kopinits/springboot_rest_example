package com.markus.app.repository.specification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.markus.app.model.filter.FilterCriteria;

public class SpecificationsSupport {
	private interface PredicateBuilder<T> {
		Predicate predicate(Expression<T> expression, FilterCriteria<T> criteria, CriteriaBuilder builder)
				throws AssertionError;
	}

	private static final Map<Class<?>, PredicateBuilder<?>> PREDICATE_BUILDERS = new HashMap<Class<?>, PredicateBuilder<?>>();

	/**
	 * Instantiates a new specifications support.
	 */
	private SpecificationsSupport() {
		// avoid initialization
	}

	static {
		PREDICATE_BUILDERS.put(BigDecimal.class, new PredicateBuilder<BigDecimal>() {
			@Override
			public Predicate predicate(final Expression<BigDecimal> expression,
					final FilterCriteria<BigDecimal> criteria, final CriteriaBuilder builder) throws AssertionError {
				switch (criteria.getQueryOption()) {
				case EQ:
					return builder.equal(expression, criteria.getValue());
				case NEQ:
					return builder.notEqual(expression, criteria.getValue());
				case LT:
					return builder.lessThan(expression, criteria.getValue());
				case GT:
					return builder.greaterThan(expression, criteria.getValue());
				default:
					return null;
				}
			}
		});
		PREDICATE_BUILDERS.put(Integer.class, new PredicateBuilder<Integer>() {
			@Override
			public Predicate predicate(final Expression<Integer> expression, final FilterCriteria<Integer> criteria,
					final CriteriaBuilder builder) throws AssertionError {
				switch (criteria.getQueryOption()) {
				case EQ:
					return builder.equal(expression, criteria.getValue());
				case NEQ:
					return builder.notEqual(expression, criteria.getValue());
				case LT:
					return builder.lessThan(expression, criteria.getValue());
				case GT:
					return builder.greaterThan(expression, criteria.getValue());
				default:
					return null;
				}
			}
		});
		PREDICATE_BUILDERS.put(Long.class, new PredicateBuilder<Long>() {
			@Override
			public Predicate predicate(final Expression<Long> expression, final FilterCriteria<Long> criteria,
					final CriteriaBuilder builder) throws AssertionError {
				switch (criteria.getQueryOption()) {
				case EQ:
					return builder.equal(expression, criteria.getValue());
				case NEQ:
					return builder.notEqual(expression, criteria.getValue());
				case LT:
					return builder.lessThan(expression, criteria.getValue());
				case GT:
					return builder.greaterThan(expression, criteria.getValue());
				default:
					return null;
				}
			}
		});
		PREDICATE_BUILDERS.put(String.class, new PredicateBuilder<String>() {
			@Override
			public Predicate predicate(final Expression<String> expression, final FilterCriteria<String> criteria,
					final CriteriaBuilder builder) throws AssertionError {
				switch (criteria.getQueryOption()) {
				case LIKE:
					return builder.like(builder.lower(expression), like(criteria.getValue())); // select
																								// and
																								// compare
																								// everything
																								// in
																								// lower
																								// case
				default:
					return null;
				}
			}
		});
		PREDICATE_BUILDERS.put(Boolean.class, new PredicateBuilder<Boolean>() {
			@Override
			public Predicate predicate(final Expression<Boolean> expression, final FilterCriteria<Boolean> criteria,
					final CriteriaBuilder builder) throws AssertionError {
				switch (criteria.getQueryOption()) {
				case EQ:
					return builder.equal(expression, criteria.getValue());
				default:
					return null;
				}
			}
		});
		PREDICATE_BUILDERS.put(DateTime.class, new PredicateBuilder<DateTime>() {
			@Override
			public Predicate predicate(final Expression<DateTime> expression, final FilterCriteria<DateTime> criteria,
					final CriteriaBuilder builder) throws AssertionError {
				switch (criteria.getQueryOption()) {
				case EQ:
					// exactly this timestamp
					return builder.equal(expression, criteria.getValue().withTimeAtStartOfDay());
				case NEQ:
					// exactly this timestamp
					return builder.not(builder.equal(expression, criteria.getValue().withTimeAtStartOfDay()));
				case LT:
					return builder.lessThan(expression, criteria.getValue());
				case GT:
					return builder.greaterThan(expression, criteria.getValue());
				default:
					return null;
				}
			}
		});
	}

	public static <T, X> void addSpecification(final List<Specification<T>> specifications, final Class<T> entityClass,
			final Class<X> attrClass, final FilterCriteria<X> criteria, final String... path) {
		if (criteria.isSet()) {
			specifications.add(buildSpecification(attrClass, criteria, path));
		}
	}

	private static <T, X> Specification<T> buildSpecification(final Class<X> attrClazz,
			final FilterCriteria<X> criteria, final String... path) {
		return new Specification<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {
				Path<?> expressionPath = root;
				for (String pathElement : path) {
					expressionPath = expressionPath.get(pathElement);
				}
				if (Enum.class.isAssignableFrom(attrClazz)) {
					return SpecificationsSupport.enumPredicate(expressionPath, (FilterCriteria<Enum<?>>) criteria,
							builder);
				} else {
					return SpecificationsSupport.buildPredicate(attrClazz, (Expression<X>) expressionPath, criteria,
							builder);
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <T> Predicate buildPredicate(final Class<T> clazz, final Expression<T> expression,
			final FilterCriteria<T> criteria, final CriteriaBuilder builder) {
		final PredicateBuilder<T> predicateBuilder = (PredicateBuilder<T>) PREDICATE_BUILDERS.get(clazz);
		if (predicateBuilder == null) {
			throw new IllegalArgumentException("Type " + clazz.getSimpleName() + " is not supported.");
		}
		final Predicate predicate = predicateBuilder.predicate(expression, criteria, builder);
		if (predicate == null) {
			throw new IllegalArgumentException("Query option " + criteria.getQueryOption() + " undefined for "
					+ clazz.getSimpleName() + " predicates!");
		}
		return predicate;
	}

	public static Predicate enumPredicate(final Expression<?> expression,
			final FilterCriteria<? extends Enum<?>> filterCriteria, final CriteriaBuilder builder) {
		switch (filterCriteria.getQueryOption()) {
		case EQ:
			return builder.equal(expression, filterCriteria.getValue());
		case NEQ:
			return builder.notEqual(expression, filterCriteria.getValue());
		default:
			throw new IllegalArgumentException(
					"Query option " + filterCriteria.getQueryOption() + " undefined for enum predicates!");
		}
	}

	/**
	 * Checks if is null predicate.
	 * 
	 * @param expression
	 *            the expression
	 * @param builder
	 *            the builder
	 * @return the predicate
	 */
	public static Predicate isSetPredicate(final Expression<?> expression, final boolean state,
			final CriteriaBuilder builder) {
		return state ? builder.isNotNull(expression) : builder.isNull(expression);
	}

	/**
	 * Like.
	 * 
	 * @param searchTerm
	 *            the search term
	 * @return the string
	 */
	private static String like(final String searchTerm) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("%");
		pattern.append(searchTerm.toLowerCase());
		pattern.append("%");
		return pattern.toString();
	}

}
