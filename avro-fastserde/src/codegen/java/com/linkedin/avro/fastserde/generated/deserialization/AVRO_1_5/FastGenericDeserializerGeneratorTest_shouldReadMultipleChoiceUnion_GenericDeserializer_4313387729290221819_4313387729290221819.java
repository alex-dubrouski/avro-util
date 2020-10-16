
package com.linkedin.avro.fastserde.generated.deserialization.AVRO_1_5;

import java.io.IOException;
import com.linkedin.avro.fastserde.FastDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;
import org.apache.avro.io.Decoder;
import org.apache.avro.util.Utf8;

public class FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion_GenericDeserializer_4313387729290221819_4313387729290221819
    implements FastDeserializer<IndexedRecord>
{

    private final Schema readerSchema;
    private final Schema union0;
    private final Schema unionOptionSchema0;
    private final Schema subField0;

    public FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion_GenericDeserializer_4313387729290221819_4313387729290221819(Schema readerSchema) {
        this.readerSchema = readerSchema;
        this.union0 = readerSchema.getField("union").schema();
        this.unionOptionSchema0 = union0 .getTypes().get(1);
        this.subField0 = unionOptionSchema0 .getField("subField").schema();
    }

    public IndexedRecord deserialize(IndexedRecord reuse, Decoder decoder)
        throws IOException
    {
        return deserializeFastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion0((reuse), (decoder));
    }

    public IndexedRecord deserializeFastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion0(Object reuse, Decoder decoder)
        throws IOException
    {
        IndexedRecord FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion;
        if ((((reuse)!= null)&&((reuse) instanceof IndexedRecord))&&(((IndexedRecord)(reuse)).getSchema() == readerSchema)) {
            FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion = ((IndexedRecord)(reuse));
        } else {
            FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion = new org.apache.avro.generic.GenericData.Record(readerSchema);
        }
        int unionIndex0 = (decoder.readIndex());
        switch (unionIndex0) {
            case  0 :
                decoder.readNull();
                break;
            case  1 :
                FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion.put(0, deserializesubRecord0(FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion.get(0), (decoder)));
                break;
            case  2 :
            {
                Object oldString1 = FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion.get(0);
                if (oldString1 instanceof Utf8) {
                    FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion.put(0, (decoder).readString(((Utf8) oldString1)));
                } else {
                    FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion.put(0, (decoder).readString(null));
                }
                break;
            }
            case  3 :
                FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion.put(0, (decoder.readInt()));
                break;
            default:
                throw new RuntimeException(("Illegal union index for 'union': "+ unionIndex0));
        }
        return FastGenericDeserializerGeneratorTest_shouldReadMultipleChoiceUnion;
    }

    public IndexedRecord deserializesubRecord0(Object reuse, Decoder decoder)
        throws IOException
    {
        IndexedRecord subRecord;
        if ((((reuse)!= null)&&((reuse) instanceof IndexedRecord))&&(((IndexedRecord)(reuse)).getSchema() == unionOptionSchema0)) {
            subRecord = ((IndexedRecord)(reuse));
        } else {
            subRecord = new org.apache.avro.generic.GenericData.Record(unionOptionSchema0);
        }
        int unionIndex1 = (decoder.readIndex());
        switch (unionIndex1) {
            case  0 :
                decoder.readNull();
                break;
            case  1 :
            {
                Object oldString0 = subRecord.get(0);
                if (oldString0 instanceof Utf8) {
                    subRecord.put(0, (decoder).readString(((Utf8) oldString0)));
                } else {
                    subRecord.put(0, (decoder).readString(null));
                }
                break;
            }
            default:
                throw new RuntimeException(("Illegal union index for 'subField': "+ unionIndex1));
        }
        return subRecord;
    }

}
