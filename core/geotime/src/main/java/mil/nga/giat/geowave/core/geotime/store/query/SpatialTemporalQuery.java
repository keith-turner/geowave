package mil.nga.giat.geowave.core.geotime.store.query;

import java.util.Date;

import mil.nga.giat.geowave.core.geotime.GeometryUtils;
import mil.nga.giat.geowave.core.geotime.index.dimension.TimeDefinition;
import mil.nga.giat.geowave.core.geotime.store.filter.SpatialQueryFilter.CompareOperation;
import mil.nga.giat.geowave.core.index.sfc.data.NumericRange;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The Spatial Temporal Query class represents a query in three dimensions. The
 * constraint that is applied represents an intersection operation on the query
 * geometry AND a date range intersection based on startTime and endTime.
 * 
 * 
 */
public class SpatialTemporalQuery extends
		SpatialQuery
{
	protected SpatialTemporalQuery() {}

	public SpatialTemporalQuery(
			final Date startTime,
			final Date endTime,
			final Geometry queryGeometry ) {
		super(
				createSpatialTemporalConstraints(
						startTime,
						endTime,
						queryGeometry),
				queryGeometry);
	}

	public SpatialTemporalQuery(
			final TemporalConstraints constraints,
			final Geometry queryGeometry ) {
		super(
				createSpatialTemporalConstraints(
						constraints,
						queryGeometry),
				queryGeometry);
	}

	public SpatialTemporalQuery(
			final Date startTime,
			final Date endTime,
			final Geometry queryGeometry,
			final CompareOperation compareOp ) {
		super(
				createSpatialTemporalConstraints(
						startTime,
						endTime,
						queryGeometry),
				queryGeometry,
				compareOp);
	}

	public SpatialTemporalQuery(
			final TemporalConstraints constraints,
			final Geometry queryGeometry,
			final CompareOperation compareOp ) {
		super(
				createSpatialTemporalConstraints(
						constraints,
						queryGeometry),
				queryGeometry,
				compareOp);
	}

	public static Constraints createConstraints(
			final TemporalRange temporalRange,
			final boolean isDefault ) {
		final Constraints constraints = new Constraints();
		constraints.addConstraint(
				TimeDefinition.class,
				new ConstraintData(
						new NumericRange(
								temporalRange.getStartTime().getTime(),
								temporalRange.getEndTime().getTime()),
						isDefault));

		return constraints;
	}

	public static Constraints createConstraints(
			final TemporalConstraints temporalConstraints,
			final boolean isDefault ) {
		final Constraints constraints = new Constraints();
		for (final TemporalRange range : temporalConstraints.getRanges()) {
			constraints.addConstraint(
					TimeDefinition.class,
					new ConstraintData(
							new NumericRange(
									range.getStartTime().getTime(),
									range.getEndTime().getTime()),
							isDefault));
		}
		return constraints;
	}

	private static Constraints createSpatialTemporalConstraints(
			final TemporalConstraints temporalConstraints,
			final Geometry queryGeometry ) {
		final Constraints geoConstraints = GeometryUtils.basicConstraintsFromGeometry(queryGeometry);
		final Constraints timeConstraints = createConstraints(
				temporalConstraints,
				false);
		return geoConstraints.merge(timeConstraints);
	}

	private static Constraints createSpatialTemporalConstraints(
			final Date startTime,
			final Date endTime,
			final Geometry queryGeometry ) {
		final Constraints geoConstraints = GeometryUtils.basicConstraintsFromGeometry(queryGeometry);
		geoConstraints.addConstraint(
				TimeDefinition.class,
				new ConstraintData(
						new NumericRange(
								startTime.getTime(),
								endTime.getTime()),
						false));
		return geoConstraints;
	}

}
