/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package mil.nga.giat.geowave.adapter.vector.simpleFeature.avro;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class AvroSimpleFeature extends
		org.apache.avro.specific.SpecificRecordBase implements
		org.apache.avro.specific.SpecificRecord
{
	public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser()
			.parse("{\"type\":\"record\",\"name\":\"AvroSimpleFeature\",\"namespace\":\"mil.nga.giat.geowave.adapter.vector.simpleFeature.avro\",\"fields\":[{\"name\":\"FeatureType\",\"type\":{\"type\":\"record\",\"name\":\"FeatureDefinition\",\"fields\":[{\"name\":\"FeatureTypeName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"AttributeNames\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}},{\"name\":\"AttributeTypes\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}},{\"name\":\"AttributeDefaultClassifications\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}}]}},{\"name\":\"Value\",\"type\":{\"type\":\"record\",\"name\":\"AttributeValue\",\"fields\":[{\"name\":\"fid\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"values\",\"type\":{\"type\":\"array\",\"items\":\"bytes\"}},{\"name\":\"classifications\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}]}}]}");

	public static org.apache.avro.Schema getClassSchema() {
		return SCHEMA$;
	}

	@Deprecated
	public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition FeatureType;
	@Deprecated
	public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue Value;

	/**
	 * Default constructor. Note that this does not initialize fields to their
	 * default values from the schema. If that is desired then one should use
	 * <code>newBuilder()</code>.
	 */
	public AvroSimpleFeature() {}

	/**
	 * All-args constructor.
	 */
	public AvroSimpleFeature(
			mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition FeatureType,
			mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue Value ) {
		this.FeatureType = FeatureType;
		this.Value = Value;
	}

	public org.apache.avro.Schema getSchema() {
		return SCHEMA$;
	}

	// Used by DatumWriter. Applications should not call.
	public java.lang.Object get(
			int field$ ) {
		switch (field$) {
			case 0:
				return FeatureType;
			case 1:
				return Value;
			default:
				throw new org.apache.avro.AvroRuntimeException(
						"Bad index");
		}
	}

	// Used by DatumReader. Applications should not call.
	@SuppressWarnings(value = "unchecked")
	public void put(
			int field$,
			java.lang.Object value$ ) {
		switch (field$) {
			case 0:
				FeatureType = (mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition) value$;
				break;
			case 1:
				Value = (mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue) value$;
				break;
			default:
				throw new org.apache.avro.AvroRuntimeException(
						"Bad index");
		}
	}

	/**
	 * Gets the value of the 'FeatureType' field.
	 */
	public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition getFeatureType() {
		return FeatureType;
	}

	/**
	 * Sets the value of the 'FeatureType' field.
	 * 
	 * @param value
	 *            the value to set.
	 */
	public void setFeatureType(
			mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition value ) {
		this.FeatureType = value;
	}

	/**
	 * Gets the value of the 'Value' field.
	 */
	public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue getValue() {
		return Value;
	}

	/**
	 * Sets the value of the 'Value' field.
	 * 
	 * @param value
	 *            the value to set.
	 */
	public void setValue(
			mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue value ) {
		this.Value = value;
	}

	/** Creates a new AvroSimpleFeature RecordBuilder */
	public static mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder newBuilder() {
		return new mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder();
	}

	/**
	 * Creates a new AvroSimpleFeature RecordBuilder by copying an existing
	 * Builder
	 */
	public static mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder newBuilder(
			mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder other ) {
		return new mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder(
				other);
	}

	/**
	 * Creates a new AvroSimpleFeature RecordBuilder by copying an existing
	 * AvroSimpleFeature instance
	 */
	public static mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder newBuilder(
			mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature other ) {
		return new mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder(
				other);
	}

	/**
	 * RecordBuilder for AvroSimpleFeature instances.
	 */
	public static class Builder extends
			org.apache.avro.specific.SpecificRecordBuilderBase<AvroSimpleFeature> implements
			org.apache.avro.data.RecordBuilder<AvroSimpleFeature>
	{

		private mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition FeatureType;
		private mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue Value;

		/** Creates a new Builder */
		private Builder() {
			super(
					mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.SCHEMA$);
		}

		/** Creates a Builder by copying an existing Builder */
		private Builder(
				mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder other ) {
			super(
					other);
			if (isValidValue(
					fields()[0],
					other.FeatureType)) {
				this.FeatureType = data().deepCopy(
						fields()[0].schema(),
						other.FeatureType);
				fieldSetFlags()[0] = true;
			}
			if (isValidValue(
					fields()[1],
					other.Value)) {
				this.Value = data().deepCopy(
						fields()[1].schema(),
						other.Value);
				fieldSetFlags()[1] = true;
			}
		}

		/** Creates a Builder by copying an existing AvroSimpleFeature instance */
		private Builder(
				mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature other ) {
			super(
					mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.SCHEMA$);
			if (isValidValue(
					fields()[0],
					other.FeatureType)) {
				this.FeatureType = data().deepCopy(
						fields()[0].schema(),
						other.FeatureType);
				fieldSetFlags()[0] = true;
			}
			if (isValidValue(
					fields()[1],
					other.Value)) {
				this.Value = data().deepCopy(
						fields()[1].schema(),
						other.Value);
				fieldSetFlags()[1] = true;
			}
		}

		/** Gets the value of the 'FeatureType' field */
		public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition getFeatureType() {
			return FeatureType;
		}

		/** Sets the value of the 'FeatureType' field */
		public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder setFeatureType(
				mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition value ) {
			validate(
					fields()[0],
					value);
			this.FeatureType = value;
			fieldSetFlags()[0] = true;
			return this;
		}

		/** Checks whether the 'FeatureType' field has been set */
		public boolean hasFeatureType() {
			return fieldSetFlags()[0];
		}

		/** Clears the value of the 'FeatureType' field */
		public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder clearFeatureType() {
			FeatureType = null;
			fieldSetFlags()[0] = false;
			return this;
		}

		/** Gets the value of the 'Value' field */
		public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue getValue() {
			return Value;
		}

		/** Sets the value of the 'Value' field */
		public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder setValue(
				mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue value ) {
			validate(
					fields()[1],
					value);
			this.Value = value;
			fieldSetFlags()[1] = true;
			return this;
		}

		/** Checks whether the 'Value' field has been set */
		public boolean hasValue() {
			return fieldSetFlags()[1];
		}

		/** Clears the value of the 'Value' field */
		public mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AvroSimpleFeature.Builder clearValue() {
			Value = null;
			fieldSetFlags()[1] = false;
			return this;
		}

		@Override
		public AvroSimpleFeature build() {
			try {
				AvroSimpleFeature record = new AvroSimpleFeature();
				record.FeatureType = fieldSetFlags()[0] ? this.FeatureType : (mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.FeatureDefinition) defaultValue(fields()[0]);
				record.Value = fieldSetFlags()[1] ? this.Value : (mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.AttributeValue) defaultValue(fields()[1]);
				return record;
			}
			catch (Exception e) {
				throw new org.apache.avro.AvroRuntimeException(
						e);
			}
		}
	}
}
