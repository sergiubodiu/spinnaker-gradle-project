package com.netflix.spinnaker.orca.pipeline.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.netflix.spinnaker.orca.pipeline.model.support.TriggerDeserializer;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Trigger {
   String getType();

   String getCorrelationId();
   String getUser();
   Map getParameters();
   List getArtifacts();

   List getNotifications();

   boolean isRebake();

   void setRebake(boolean var1);

   boolean isDryRun();

   void setDryRun(boolean var1);

   boolean isStrategy();

   void setStrategy(boolean var1);

   List getResolvedExpectedArtifacts();

   void setResolvedExpectedArtifacts(List var1);

   @JsonAnyGetter
   Map getOther();

   @JsonAnySetter
   void setOther(Map var1);
}
