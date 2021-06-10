package com.dametto.itinerary_server.utils.path;

import com.dametto.itinerary_server.jpa.TouristAttraction;
import com.dametto.itinerary_server.jpa.TravelPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TravelPathDataStr {
    private TravelNodeDataStr head = null;
    private TravelNodeDataStr tail = null;
    private Float totalMinutes = 0f;

    // HashMap used to improve efficiency of common operations
    HashMap<Integer, TravelNodeDataStr> travelNodeHashMap;

    public TravelPathDataStr() {
        travelNodeHashMap = new HashMap<>();
    }

    // O(1) time complexity
    public void addTravelNode(TouristAttraction attraction) {
        TravelNodeDataStr travelNodeDataStr = new TravelNodeDataStr(attraction);
        if(head == null && tail == null) {
            // First element
            this.head = travelNodeDataStr;
            this.tail = travelNodeDataStr;

            // Update total minutes
            // Just add attraction visit time
            totalMinutes += attraction.getVisitsDurationMinutes();
        }
        else {
            travelNodeDataStr.addPredecessor(tail);
            tail.addSuccessor(travelNodeDataStr);
            this.tail = travelNodeDataStr;

            // Update total minutes
            // Add attraction visit time
            totalMinutes += attraction.getVisitsDurationMinutes();
        }
        // update hashmap
        travelNodeHashMap.put(attraction.getId(), travelNodeDataStr);
    }

    // O(1) time complexity
    public void removeTravelNode(TouristAttraction attraction) {
        TravelNodeDataStr travelNodeDataStr = travelNodeHashMap.get(attraction.getId());

        TravelEdgeDataStr predecessorEdge = travelNodeDataStr.getPredEdge();
        TravelEdgeDataStr successorEdge = travelNodeDataStr.getSuccEdge();

        if(predecessorEdge == null && successorEdge == null) {
            // Only one element in the list
            head = tail = null;
            totalMinutes = 0f;
            travelNodeHashMap.clear();
        }
        else if(predecessorEdge == null) {
            // It's the head
            head = successorEdge.getSecond();
            head.setPredEdge(null);

            travelNodeHashMap.remove(attraction.getId());
            totalMinutes = totalMinutes - travelNodeDataStr.getTouristAttraction().getVisitsDurationMinutes();
        }
        else if(successorEdge == null) {
            // Tail
            tail = predecessorEdge.getFirst();
            tail.setSuccEdge(null);

            travelNodeHashMap.remove(attraction.getId());
            totalMinutes = totalMinutes - travelNodeDataStr.getTouristAttraction().getVisitsDurationMinutes();
        }
        else {
            TravelNodeDataStr newFirst = predecessorEdge.getFirst();
            TravelNodeDataStr newEnd = successorEdge.getSecond();

            newFirst.addSuccessor(newEnd);
            newEnd.addPredecessor(newFirst);
            // Update hashmap
            travelNodeHashMap.remove(attraction.getId());

            // Update total minutes
            // Remove attraction visiti time
            totalMinutes = totalMinutes - attraction.getVisitsDurationMinutes();
        }
    }

    public TouristAttraction getPredecessor(TouristAttraction attraction) {
        TravelNodeDataStr node = travelNodeHashMap.get(attraction.getId());
        return node == null || node.getPredEdge() == null ? null : node.getPredEdge().getFirst().getTouristAttraction();
    }

    public TouristAttraction getSuccessor(TouristAttraction attraction) {
        TravelNodeDataStr node = travelNodeHashMap.get(attraction.getId());
        return node == null || node.getSuccEdge() == null ? null : node.getSuccEdge().getSecond().getTouristAttraction();
    }

    public TouristAttraction getLastAttraction() {
        return tail == null ? null : tail.getTouristAttraction();
    }

    public Float getTotalMinutes() {
        return totalMinutes;
    }

    public Boolean isEmpty() {
        return head == null && tail == null;
    }

    public static ArrayList<TravelPathDataStr> divideInDays(TravelPathDataStr path, Integer minutesPerday) {
        ArrayList<TravelPathDataStr> travelPathDataStrs = new ArrayList<>();

        TravelPathDataStr currentPath = new TravelPathDataStr();
        travelPathDataStrs.add(currentPath);
        while (!path.isEmpty()) {
            TravelNodeDataStr node = path.head;
            // remove from generic path
            path.removeTravelNode(node.getTouristAttraction());

            if((currentPath.getTotalMinutes() + node.getTouristAttraction().getVisitsDurationMinutes()) > minutesPerday) {
                // Create new day
                currentPath = new TravelPathDataStr();
                travelPathDataStrs.add(currentPath);
            }

            TouristAttraction lastAttraction = currentPath.getLastAttraction();

            currentPath.addTravelNode(node.getTouristAttraction());
        }

        return travelPathDataStrs;
    }

    public List<TouristAttraction> getTravelPath() {
        List<TouristAttraction> attractions = new ArrayList<>();

        TravelNodeDataStr toGo = this.head;
        while(toGo != null) {
            attractions.add(toGo.getTouristAttraction());

            if(toGo.getSuccEdge() == null) {
                toGo = null;
            }
            else {
                toGo = toGo.getSuccEdge().getSecond();
            }
        }

        return attractions;
    }
}
