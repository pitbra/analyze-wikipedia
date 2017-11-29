/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.ui.webui.contracts;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Pit.Braunsdorf
 */
public class ArborViewModel {
    private List<EntityViewModel> nodes;
    private Hashtable<Long, HashSet<Long>> edges;
    
    private static final String SHAPE = "dot";

    public ArborViewModel() {
    }

    public List<EntityViewModel> getNodes() {
        return nodes;
    }

    public void setNodes(List<EntityViewModel> nodes) {
        this.nodes = nodes;
    }

    public Hashtable<Long, HashSet<Long>> getEdges() {
        return edges;
    }

    public void setEdges(Hashtable<Long, HashSet<Long>> edges) {
        this.edges = edges;
    }
    
    public void AddNode(EntityViewModel node){
        this.nodes.add(node);
    }
        
    public String toJson() {
        String result = String.format("{%s,%s}", NodesToJson(), EdgesToJson() );
        
        return result;
    }
    
    public String NodesToJson() {
        String nodeResult = "";
        
        for(EntityViewModel node : nodes) {
            nodeResult += String.format("\"%s\":{\"color\":\"%s\", \"shape\":\"%s\", \"label\": \"%s\"}", node.getId(), getColorForType(node.getType()), SHAPE, node.getName() );
            
            if(nodes.size()-1 != nodes.indexOf(node)) {
                nodeResult += ",";
            }
        }
        
        return String.format("\"nodes\":{%s}", nodeResult);
    }
    
    public String EdgesToJson() {
        String edgeResult = "";
        
        int i = 0;
        for(Long edge: edges.keySet()) {
            String toNodes = "";
            
            int j = 0;
            for(Long to : edges.get(edge)) {
                toNodes += String.format("\"%d\":{}", to);
                
                if(edges.get(edge).size() - 1> j) {
                    toNodes += ",";
                }
                ++j;
            }
            
            
            edgeResult += String.format("\"%d\":{%s}", edge, toNodes);
            
            if(edges.size()-1 > i) {
                edgeResult += ",";
            }
            ++i;
        }
        
        return String.format("\"edges\":{%s}", edgeResult);
    }
    
    public String getColorForType(String type) {
        return "red";
    }
}
