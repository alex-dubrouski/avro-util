/*
 * Copyright 2020 LinkedIn Corp.
 * Licensed under the BSD 2-Clause License (the "License").
 * See License in the project root for license information.
 */

package com.linkedin.avroutil1.compatibility;

import com.linkedin.avroutil1.testcommon.TestUtil;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.SchemaParseException;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * tests the schema parsing methods on the {@link AvroCompatibilityHelper} class
 */
public class AvroCompatibilityHelperParsingTest {

  @Test
  public void testParseSimpleSchema() throws Exception {
    String avsc = TestUtil.load("PerfectlyNormalRecord.avsc");
    SchemaParseResult result = AvroCompatibilityHelper.parse(avsc, null, null);
    verifySimpleSchema(result);
  }

  @Test
  public void testParseSimpleSchemaAsInputStream() throws Exception {
    String avsc = TestUtil.load("PerfectlyNormalRecord.avsc");
    byte[] utf8 = avsc.getBytes(StandardCharsets.UTF_8);
    SchemaParseResult result = AvroCompatibilityHelper.parse(new ByteArrayInputStream(utf8), null, null);
    verifySimpleSchema(result);
  }

  private void verifySimpleSchema(SchemaParseResult result) {
    Assert.assertNotNull(result);
    Schema mainSchema = result.getMainSchema();
    Assert.assertNotNull(mainSchema);
    Assert.assertEquals(mainSchema.getFullName(), "com.acme.PerfectlyNormalRecord");
    Map<String, Schema> allSchemas = result.getAllSchemas();
    Assert.assertEquals(allSchemas.size(), 1);
    Assert.assertEquals(mainSchema, allSchemas.get(mainSchema.getFullName()));
  }

  //TODO - add a test for validating namespaces once https://issues.apache.org/jira/browse/AVRO-2742 is settled

  @Test(expectedExceptions = SchemaParseException.class)
  public void testValidateRecordNames() throws Exception {
    String avsc = TestUtil.load("RecordWithInvalidName.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(true, false);
    AvroCompatibilityHelper.parse(avsc, conf, null);
  }

  @Test
  public void testDontValidateRecordNames() throws Exception {
    String avsc = TestUtil.load("RecordWithInvalidName.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, true);
    SchemaParseResult result = AvroCompatibilityHelper.parse(avsc, conf, null);
    Assert.assertNotNull(result.getMainSchema());
  }

  @Test(expectedExceptions = SchemaParseException.class)
  public void testValidateFieldNames() throws Exception {
    String avsc = TestUtil.load("RecordWithInvalidFieldName.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(true, false);
    AvroCompatibilityHelper.parse(avsc, conf, null);
  }

  @Test
  public void testDontValidateFieldNames() throws Exception {
    String avsc = TestUtil.load("RecordWithInvalidFieldName.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, false);
    SchemaParseResult result = AvroCompatibilityHelper.parse(avsc, conf, null);
    Assert.assertNotNull(result.getMainSchema());
  }

  @Test(expectedExceptions = AvroTypeException.class)
  public void testValidateFieldDefaults() throws Exception {
    String avsc = TestUtil.load("RecordWithInvalidFieldDefaultValue.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, true);
    AvroCompatibilityHelper.parse(avsc, conf, null);
  }

  @Test
  public void testDontValidateFieldDefaults() throws Exception {
    String avsc = TestUtil.load("RecordWithInvalidFieldDefaultValue.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, false);
    SchemaParseResult result = AvroCompatibilityHelper.parse(avsc, conf, null);
    Assert.assertNotNull(result.getMainSchema());
  }

  @Test(expectedExceptions = AvroTypeException.class)
  public void testValidateUnionDefaults() throws Exception {
    String avsc = TestUtil.load("RecordWithInvalidUnionDefault.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, true);
    AvroCompatibilityHelper.parse(avsc, conf, null);
  }

  @Test
  public void testDontValidateUnionDefaults() throws Exception {
    String avsc = TestUtil.load("RecordWithInvalidUnionDefault.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, false);
    SchemaParseResult result = AvroCompatibilityHelper.parse(avsc, conf, null);
    Assert.assertNotNull(result.getMainSchema());
  }

  @Test(expectedExceptions = SchemaParseException.class)
  public void testValidateFixedNames() throws Exception {
    String avsc = TestUtil.load("FixedWithInvalidName.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(true, false);
    AvroCompatibilityHelper.parse(avsc, conf, null);
  }

  @Test
  public void testDontValidateFixedNames() throws Exception {
    String avsc = TestUtil.load("FixedWithInvalidName.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, false);
    SchemaParseResult result = AvroCompatibilityHelper.parse(avsc, conf, null);
    Assert.assertNotNull(result.getMainSchema());
  }

  @Test(expectedExceptions = SchemaParseException.class)
  public void testValidateEnumNames() throws Exception {
    String avsc = TestUtil.load("EnumWithInvalidName.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(true, false);
    AvroCompatibilityHelper.parse(avsc, conf, null);
  }

  @Test
  public void testDontValidateEnumNames() throws Exception {
    String avsc = TestUtil.load("EnumWithInvalidName.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, false);
    SchemaParseResult result = AvroCompatibilityHelper.parse(avsc, conf, null);
    Assert.assertNotNull(result.getMainSchema());
  }

  @Test(expectedExceptions = SchemaParseException.class)
  public void testValidateEnumValues() throws Exception {
    String avsc = TestUtil.load("EnumWithInvalidValue.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(true, false);
    AvroCompatibilityHelper.parse(avsc, conf, null);
  }

  @Test
  public void testDontValidateEnumValues() throws Exception {
    String avsc = TestUtil.load("EnumWithInvalidValue.avsc");
    SchemaParseConfiguration conf = new SchemaParseConfiguration(false, false);
    SchemaParseResult result = AvroCompatibilityHelper.parse(avsc, conf, null);
    Assert.assertNotNull(result.getMainSchema());
  }

  @Test
  public void testParseWithExternalRef() throws Exception {
    String innerAvsc = TestUtil.load("allavro/InnerRecord.avsc");
    String outerAvsc = TestUtil.load("allavro/OuterRecord.avsc");
    Schema innerSchema = AvroCompatibilityHelper.parse(innerAvsc);
    SchemaParseResult result =
        AvroCompatibilityHelper.parse(outerAvsc, SchemaParseConfiguration.STRICT, Arrays.asList(innerSchema));
    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.getAllSchemas().size());
    Schema outerSchema = result.getMainSchema();
    Assert.assertEquals(outerSchema.getFullName(), "allavro.OuterRecord");
    Assert.assertEquals(outerSchema.getField("f").schema().getTypes().get(1).getFullName(), "allavro.InnerRecord");
    Assert.assertTrue(result.getAllSchemas().containsKey("allavro.InnerRecord"));
    Assert.assertEquals(result.getAllSchemas().get("allavro.InnerRecord"), innerSchema);
  }

  @Test
  public void testParseWithExternalRefAndInferredNamespace() throws Exception {
    String innerAvsc = TestUtil.load("allavro/InnerRecord.avsc");
    String outerAvsc = TestUtil.load("allavro/OuterRecordWithInferredInnerRecordNamespace.avsc");
    Schema innerSchema = AvroCompatibilityHelper.parse(innerAvsc);
    SchemaParseResult result =
        AvroCompatibilityHelper.parse(outerAvsc, SchemaParseConfiguration.STRICT, Arrays.asList(innerSchema));
    Assert.assertNotNull(result);
    Assert.assertEquals(2, result.getAllSchemas().size());
    Schema outerSchema = result.getMainSchema();
    Assert.assertEquals(outerSchema.getFullName(), "allavro.OuterRecord");
    Assert.assertEquals(outerSchema.getField("f").schema().getTypes().get(1).getFullName(), "allavro.InnerRecord");
    Assert.assertTrue(result.getAllSchemas().containsKey("allavro.InnerRecord"));
    Assert.assertEquals(result.getAllSchemas().get("allavro.InnerRecord"), innerSchema);
  }

  @Test
  public void testParsingLooseNumericDefaults() throws Exception {
    String badFloatAvsc = "{\"type\": \"record\", \"name\": \"Rec\", \"fields\": [{\"name\": \"f\", \"type\": \"float\", \"default\": 0}]}";
    String badDoubleAvsc = "{\"type\": \"record\", \"name\": \"Rec\", \"fields\": [{\"name\": \"f\", \"type\": \"double\", \"default\": 0}]}";
    String badIntAvsc = "{\"type\": \"record\", \"name\": \"Rec\", \"fields\": [{\"name\": \"f\", \"type\": \"int\", \"default\": 0.0}]}";
    String badLongAvsc = "{\"type\": \"record\", \"name\": \"Rec\", \"fields\": [{\"name\": \"f\", \"type\": \"long\", \"default\": 0.0}]}";

    List<String> allSchemas = Arrays.asList(badFloatAvsc, badDoubleAvsc, badIntAvsc, badLongAvsc);

    for (String avsc : allSchemas) {

      //doesnt parse as strict
      try {
        //noinspection ResultOfMethodCallIgnored
        AvroCompatibilityHelper.parse(avsc, SchemaParseConfiguration.STRICT, null).getMainSchema();
        Assert.fail("was not supposed to parse");
      } catch (AvroTypeException expected) {
        //expected
      }

      //parses under loose_numerics
      Schema parsed = AvroCompatibilityHelper.parse(avsc, SchemaParseConfiguration.LOOSE_NUMERICS, null).getMainSchema();
      Assert.assertNotNull(parsed);
    }
  }

  @Test
  public void testParsingHorribleLooseNumericDefaults() throws Exception {
    String horribleIntAvsc = "{\"type\": \"record\", \"name\": \"Rec\", \"fields\": [{\"name\": \"f\", \"type\": \"int\", \"default\": 1.5}]}";
    String horribleLongAvsc = "{\"type\": \"record\", \"name\": \"Rec\", \"fields\": [{\"name\": \"f\", \"type\": \"long\", \"default\": 3.14}]}";

    List<String> allSchemas = Arrays.asList(horribleIntAvsc, horribleLongAvsc);
    for (String avsc : allSchemas) {

      //doesnt parse as loose
      try {
        //noinspection ResultOfMethodCallIgnored
        AvroCompatibilityHelper.parse(avsc, SchemaParseConfiguration.LOOSE_NUMERICS, null).getMainSchema();
        Assert.fail("was not supposed to parse");
      } catch (AvroTypeException expected) {
        //expected
      }
    }
  }

  @Test
  public void testParseTrailingContent() throws Exception {
    String innocent = "{\"type\": \"string\"}";
    Assert.assertNotNull(AvroCompatibilityHelper.parse(innocent, SchemaParseConfiguration.STRICT, null).getMainSchema());
    Assert.assertNotNull(AvroCompatibilityHelper.parse(innocent + "  \n \t \r  ", SchemaParseConfiguration.STRICT, null).getMainSchema());
    try {
      AvroCompatibilityHelper.parse(innocent + "?", SchemaParseConfiguration.STRICT, null);
      Assert.fail("expected to fail");
    } catch (IllegalArgumentException expected) {
      Assert.assertTrue(expected.getMessage().contains("?"));
    }
    Assert.assertNotNull(AvroCompatibilityHelper.parse(innocent + "?", SchemaParseConfiguration.LOOSE, null).getMainSchema());
    try {
      AvroCompatibilityHelper.parse(innocent + "; DROP TABLE STUDENTS", SchemaParseConfiguration.STRICT, null);
      Assert.fail("expected to fail");
    } catch (IllegalArgumentException expected) {
      Assert.assertTrue(expected.getMessage().contains("DROP TABLE STUDENTS"));
    }
    Assert.assertNotNull(
        AvroCompatibilityHelper.parse(innocent + "; DROP TABLE STUDENTS", SchemaParseConfiguration.LOOSE, null).getMainSchema()
    );
  }
}
