/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package academy.kafka.serializers.examples;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class AvroPerson extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 946652956343545221L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroPerson\",\"namespace\":\"academy.kafka.serializers.examples\",\"fields\":[{\"name\":\"bsn\",\"type\":\"string\"},{\"name\":\"firstName\",\"type\":\"string\"},{\"name\":\"lastName\",\"type\":\"string\"},{\"name\":\"bancAccount\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<AvroPerson> ENCODER =
      new BinaryMessageEncoder<AvroPerson>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<AvroPerson> DECODER =
      new BinaryMessageDecoder<AvroPerson>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<AvroPerson> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<AvroPerson> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<AvroPerson> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<AvroPerson>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this AvroPerson to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a AvroPerson from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a AvroPerson instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static AvroPerson fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.lang.CharSequence bsn;
   private java.lang.CharSequence firstName;
   private java.lang.CharSequence lastName;
   private java.lang.CharSequence bancAccount;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public AvroPerson() {}

  /**
   * All-args constructor.
   * @param bsn The new value for bsn
   * @param firstName The new value for firstName
   * @param lastName The new value for lastName
   * @param bancAccount The new value for bancAccount
   */
  public AvroPerson(java.lang.CharSequence bsn, java.lang.CharSequence firstName, java.lang.CharSequence lastName, java.lang.CharSequence bancAccount) {
    this.bsn = bsn;
    this.firstName = firstName;
    this.lastName = lastName;
    this.bancAccount = bancAccount;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return bsn;
    case 1: return firstName;
    case 2: return lastName;
    case 3: return bancAccount;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: bsn = (java.lang.CharSequence)value$; break;
    case 1: firstName = (java.lang.CharSequence)value$; break;
    case 2: lastName = (java.lang.CharSequence)value$; break;
    case 3: bancAccount = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'bsn' field.
   * @return The value of the 'bsn' field.
   */
  public java.lang.CharSequence getBsn() {
    return bsn;
  }


  /**
   * Sets the value of the 'bsn' field.
   * @param value the value to set.
   */
  public void setBsn(java.lang.CharSequence value) {
    this.bsn = value;
  }

  /**
   * Gets the value of the 'firstName' field.
   * @return The value of the 'firstName' field.
   */
  public java.lang.CharSequence getFirstName() {
    return firstName;
  }


  /**
   * Sets the value of the 'firstName' field.
   * @param value the value to set.
   */
  public void setFirstName(java.lang.CharSequence value) {
    this.firstName = value;
  }

  /**
   * Gets the value of the 'lastName' field.
   * @return The value of the 'lastName' field.
   */
  public java.lang.CharSequence getLastName() {
    return lastName;
  }


  /**
   * Sets the value of the 'lastName' field.
   * @param value the value to set.
   */
  public void setLastName(java.lang.CharSequence value) {
    this.lastName = value;
  }

  /**
   * Gets the value of the 'bancAccount' field.
   * @return The value of the 'bancAccount' field.
   */
  public java.lang.CharSequence getBancAccount() {
    return bancAccount;
  }


  /**
   * Sets the value of the 'bancAccount' field.
   * @param value the value to set.
   */
  public void setBancAccount(java.lang.CharSequence value) {
    this.bancAccount = value;
  }

  /**
   * Creates a new AvroPerson RecordBuilder.
   * @return A new AvroPerson RecordBuilder
   */
  public static academy.kafka.serializers.examples.AvroPerson.Builder newBuilder() {
    return new academy.kafka.serializers.examples.AvroPerson.Builder();
  }

  /**
   * Creates a new AvroPerson RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new AvroPerson RecordBuilder
   */
  public static academy.kafka.serializers.examples.AvroPerson.Builder newBuilder(academy.kafka.serializers.examples.AvroPerson.Builder other) {
    if (other == null) {
      return new academy.kafka.serializers.examples.AvroPerson.Builder();
    } else {
      return new academy.kafka.serializers.examples.AvroPerson.Builder(other);
    }
  }

  /**
   * Creates a new AvroPerson RecordBuilder by copying an existing AvroPerson instance.
   * @param other The existing instance to copy.
   * @return A new AvroPerson RecordBuilder
   */
  public static academy.kafka.serializers.examples.AvroPerson.Builder newBuilder(academy.kafka.serializers.examples.AvroPerson other) {
    if (other == null) {
      return new academy.kafka.serializers.examples.AvroPerson.Builder();
    } else {
      return new academy.kafka.serializers.examples.AvroPerson.Builder(other);
    }
  }

  /**
   * RecordBuilder for AvroPerson instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AvroPerson>
    implements org.apache.avro.data.RecordBuilder<AvroPerson> {

    private java.lang.CharSequence bsn;
    private java.lang.CharSequence firstName;
    private java.lang.CharSequence lastName;
    private java.lang.CharSequence bancAccount;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(academy.kafka.serializers.examples.AvroPerson.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.bsn)) {
        this.bsn = data().deepCopy(fields()[0].schema(), other.bsn);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.firstName)) {
        this.firstName = data().deepCopy(fields()[1].schema(), other.firstName);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.lastName)) {
        this.lastName = data().deepCopy(fields()[2].schema(), other.lastName);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.bancAccount)) {
        this.bancAccount = data().deepCopy(fields()[3].schema(), other.bancAccount);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing AvroPerson instance
     * @param other The existing instance to copy.
     */
    private Builder(academy.kafka.serializers.examples.AvroPerson other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.bsn)) {
        this.bsn = data().deepCopy(fields()[0].schema(), other.bsn);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.firstName)) {
        this.firstName = data().deepCopy(fields()[1].schema(), other.firstName);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.lastName)) {
        this.lastName = data().deepCopy(fields()[2].schema(), other.lastName);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.bancAccount)) {
        this.bancAccount = data().deepCopy(fields()[3].schema(), other.bancAccount);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'bsn' field.
      * @return The value.
      */
    public java.lang.CharSequence getBsn() {
      return bsn;
    }


    /**
      * Sets the value of the 'bsn' field.
      * @param value The value of 'bsn'.
      * @return This builder.
      */
    public academy.kafka.serializers.examples.AvroPerson.Builder setBsn(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.bsn = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'bsn' field has been set.
      * @return True if the 'bsn' field has been set, false otherwise.
      */
    public boolean hasBsn() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'bsn' field.
      * @return This builder.
      */
    public academy.kafka.serializers.examples.AvroPerson.Builder clearBsn() {
      bsn = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'firstName' field.
      * @return The value.
      */
    public java.lang.CharSequence getFirstName() {
      return firstName;
    }


    /**
      * Sets the value of the 'firstName' field.
      * @param value The value of 'firstName'.
      * @return This builder.
      */
    public academy.kafka.serializers.examples.AvroPerson.Builder setFirstName(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.firstName = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'firstName' field has been set.
      * @return True if the 'firstName' field has been set, false otherwise.
      */
    public boolean hasFirstName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'firstName' field.
      * @return This builder.
      */
    public academy.kafka.serializers.examples.AvroPerson.Builder clearFirstName() {
      firstName = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'lastName' field.
      * @return The value.
      */
    public java.lang.CharSequence getLastName() {
      return lastName;
    }


    /**
      * Sets the value of the 'lastName' field.
      * @param value The value of 'lastName'.
      * @return This builder.
      */
    public academy.kafka.serializers.examples.AvroPerson.Builder setLastName(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.lastName = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'lastName' field has been set.
      * @return True if the 'lastName' field has been set, false otherwise.
      */
    public boolean hasLastName() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'lastName' field.
      * @return This builder.
      */
    public academy.kafka.serializers.examples.AvroPerson.Builder clearLastName() {
      lastName = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'bancAccount' field.
      * @return The value.
      */
    public java.lang.CharSequence getBancAccount() {
      return bancAccount;
    }


    /**
      * Sets the value of the 'bancAccount' field.
      * @param value The value of 'bancAccount'.
      * @return This builder.
      */
    public academy.kafka.serializers.examples.AvroPerson.Builder setBancAccount(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.bancAccount = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'bancAccount' field has been set.
      * @return True if the 'bancAccount' field has been set, false otherwise.
      */
    public boolean hasBancAccount() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'bancAccount' field.
      * @return This builder.
      */
    public academy.kafka.serializers.examples.AvroPerson.Builder clearBancAccount() {
      bancAccount = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AvroPerson build() {
      try {
        AvroPerson record = new AvroPerson();
        record.bsn = fieldSetFlags()[0] ? this.bsn : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.firstName = fieldSetFlags()[1] ? this.firstName : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.lastName = fieldSetFlags()[2] ? this.lastName : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.bancAccount = fieldSetFlags()[3] ? this.bancAccount : (java.lang.CharSequence) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<AvroPerson>
    WRITER$ = (org.apache.avro.io.DatumWriter<AvroPerson>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<AvroPerson>
    READER$ = (org.apache.avro.io.DatumReader<AvroPerson>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.bsn);

    out.writeString(this.firstName);

    out.writeString(this.lastName);

    out.writeString(this.bancAccount);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.bsn = in.readString(this.bsn instanceof Utf8 ? (Utf8)this.bsn : null);

      this.firstName = in.readString(this.firstName instanceof Utf8 ? (Utf8)this.firstName : null);

      this.lastName = in.readString(this.lastName instanceof Utf8 ? (Utf8)this.lastName : null);

      this.bancAccount = in.readString(this.bancAccount instanceof Utf8 ? (Utf8)this.bancAccount : null);

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.bsn = in.readString(this.bsn instanceof Utf8 ? (Utf8)this.bsn : null);
          break;

        case 1:
          this.firstName = in.readString(this.firstName instanceof Utf8 ? (Utf8)this.firstName : null);
          break;

        case 2:
          this.lastName = in.readString(this.lastName instanceof Utf8 ? (Utf8)this.lastName : null);
          break;

        case 3:
          this.bancAccount = in.readString(this.bancAccount instanceof Utf8 ? (Utf8)this.bancAccount : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










