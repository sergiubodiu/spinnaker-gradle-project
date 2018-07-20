package com.netflix.spinnaker.orca.pipeline.model.support

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer


public final class RequisiteStageRefIdDeserializer extends StdDeserializer {
  public Collection deserialize(JsonParser p, DeserializationContext ctxt) {
    Intrinsics.checkParameterIsNotNull(p, "p");
    Intrinsics.checkParameterIsNotNull(ctxt, "ctxt");
    Collection var10000 = (Collection)p.getCodec().readValue(p, (TypeReference)(new TypeReference() {
    }));
    return var10000 != null ? var10000 : (Collection)SetsKt.emptySet();
  }

  public RequisiteStageRefIdDeserializer() {
    super(Collection.class);
  }
}

