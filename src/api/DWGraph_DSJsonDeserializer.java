package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;


/**This class used in order to deserialize Json's graph object. Using this class enable performing deserialization of interface implementation classes.**/
public class DWGraph_DSJsonDeserializer implements JsonDeserializer<DWGraph_DS> {

    @Override
    public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        DWGraph_DS g = new DWGraph_DS();
        JsonObject JsonObj = jsonElement.getAsJsonObject();
        JsonElement nodes = JsonObj.getAsJsonArray("Nodes").getAsJsonArray();
        Iterator<JsonElement> itNodes = nodes.getAsJsonArray().iterator();
        while (itNodes.hasNext()) {
            JsonElement current = itNodes.next();
            int id = current.getAsJsonObject().get("id").getAsInt();
            String location = current.getAsJsonObject().get("pos").getAsString();
            String[] xyz = location.split(",");
            double x = Double.parseDouble(xyz[0]);
            double y = Double.parseDouble(xyz[1]);
            double z = Double.parseDouble(xyz[2]);
            DWGraph_DS.NodeData node = g.new NodeData(id, x, y, z);
            g.addNode(node);
        }
        //connecting nodes the same way as Json object
        JsonElement edges = JsonObj.getAsJsonArray("Edges").getAsJsonArray();
        Iterator<JsonElement> itEdges = edges.getAsJsonArray().iterator();
        while (itEdges.hasNext()){
            JsonElement current = itEdges.next();
            int src = current.getAsJsonObject().get("src").getAsInt();
            int dest = current.getAsJsonObject().get("dest").getAsInt();
            double edge = current.getAsJsonObject().get("w").getAsDouble();
            g.connect(src, dest, edge);
        }
        return g;
    }
}

//        //connecting nodes the same way as Json object
//        for(Map.Entry<String, JsonElement> set : allNodesObj.entrySet()){
//            JsonElement value = set.getValue();
//            JsonObject neighbors = value.getAsJsonObject().get("neighbors").getAsJsonObject();
//            //going over each node's neighbors list
//            for (Map.Entry<String, JsonElement> neSet : neighbors.entrySet()){
//                JsonElement neValue = neSet.getValue();
//                int src = neValue.getAsJsonObject().get("src").getAsInt();
//                int dest = neValue.getAsJsonObject().get("dest").getAsInt();
//                double edge = neValue.getAsJsonObject().get("weight").getAsDouble();
//                g.connect(src, dest, edge);
//            }
//        }
//
//        return g;
//    }

