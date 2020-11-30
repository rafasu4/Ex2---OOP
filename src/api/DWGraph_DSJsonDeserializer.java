package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;


/**This class used in order to deserialize Json's graph object. Using this class enable performing deserialization of interface implementation classes.**/
public class DWGraph_DSJsonDeserializer implements JsonDeserializer<DWGraph_DS> {

    @Override
    public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        DWGraph_DS g = new DWGraph_DS();
        JsonObject allNodesObj = jsonObject.get("allNodes").getAsJsonObject();//this graph's neighbors list
        //adding all nodes from Json object to g
        for (Map.Entry<String, JsonElement> set : allNodesObj.entrySet()){
            JsonElement value = set.getValue();
            int nodeKey = value.getAsJsonObject().get("key").getAsInt();
            double nodeWeight = value.getAsJsonObject().get("weight").getAsDouble();
            DWGraph_DS.NodeData node = g.new NodeData(nodeKey, nodeWeight);
            g.addNode(node);
        }
        //connecting nodes the same way as Json object
        for(Map.Entry<String, JsonElement> set : allNodesObj.entrySet()){
            JsonElement value = set.getValue();
            JsonObject neighbors = value.getAsJsonObject().get("neighbors").getAsJsonObject();
            //going over each node's neighbors list
            for (Map.Entry<String, JsonElement> neSet : neighbors.entrySet()){
                JsonElement neValue = neSet.getValue();
                int src = neValue.getAsJsonObject().get("src").getAsInt();
                int dest = neValue.getAsJsonObject().get("dest").getAsInt();
                double edge = neValue.getAsJsonObject().get("weight").getAsDouble();
                g.connect(src, dest, edge);
            }
        }

        return g;
    }
}
