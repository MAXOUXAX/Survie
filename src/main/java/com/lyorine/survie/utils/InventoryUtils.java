package com.lyorine.survie.utils;

public class InventoryUtils {

    public int getSizeOfInventoryFromSlotsNeeded(int slotNeeded){
        int[] inventorySize = new int[]{9, 9*2, 9*3, 9*4, 9*5, 9*6};
        for (int i : inventorySize) {
            if(i > slotNeeded){
                return i;
            }
        }
        return 9;
    }

}
