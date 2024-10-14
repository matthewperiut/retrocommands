package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class Ride implements Command {


    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        if (parameters.length < 3) {
            if (player.vehicle != null) {
                player.setVehicle(null);
                return;
            }
            manual(commandSource);
            return;
        }

        String rider = parameters[1];
        String vehicle = parameters[2];

        Entity riderEntity = null;
        Entity vehicleEntity = null;

        for (Object o : player.world.players) {
            PlayerEntity p = (PlayerEntity) o;
            System.out.println(p.name);
            if (p.name.equals(rider)) {
                riderEntity = p;
            }
            if (p.name.equals(vehicle)) {
                vehicleEntity = p;
            }
        }

        int riderId = -1;
        int vehicleId = -1;

        try {
            if (riderEntity == null) riderId = Integer.parseInt(rider);
            if (vehicleEntity == null) vehicleId = Integer.parseInt(vehicle);
        } catch (Exception e) {
            commandSource.sendFeedback("Invalid entity id");
        }

        for (Object o : player.world.entities) {
            Entity e = (Entity) o;

            if (e.id == riderId) {
                riderEntity = e;
            }
            if (e.id == vehicleId) {
                vehicleEntity = e;
            }
        }

        if (riderEntity == null || vehicleEntity == null) {
            commandSource.sendFeedback("Invalid entity id");
            return;
        }

        riderEntity.setVehicle(vehicleEntity);
        String riderString = riderEntity instanceof PlayerEntity ? ((PlayerEntity) riderEntity).name : "The " + (String) EntityRegistry.classToId.get(riderEntity.getClass());
        String vehicleString = vehicleEntity instanceof PlayerEntity ? ((PlayerEntity) vehicleEntity).name : "the " + (String) EntityRegistry.classToId.get(vehicleEntity.getClass());
        commandSource.sendFeedback(riderString + " is now on " + vehicleString);
    }

    @Override
    public String name() {
        return "ride";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /ride {rider entity id} {vehicle entity id}");
        commandSource.sendFeedback("Info: Puts an entity on an entity");
        commandSource.sendFeedback("You can find entity id in the F3 menu");
        commandSource.sendFeedback("You can also use player names instead");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1 || parameterNum == 2) {
            PlayerEntity p = source.getPlayer();
            List<Entity> entities = p.world.collectEntitiesByClass(Entity.class, Box.create(p.x-20, p.y-20, p.z-20, p.x+20, p.y+20, p.z+20));

            // Use TreeMap to keep entries in order based on the distance
            TreeMap<Double, Entity> distanceMap = new TreeMap<>();

            for (Entity entity : entities) {
                double distance = p.getDistance(entity);
                // Handle potential duplicates (unlikely but possible)
                while (distanceMap.containsKey(distance)) {
                    distance += 0.0001;  // Small offset to handle entities at almost same distance
                }
                distanceMap.put(distance, entity);
            }

            // Extract entity IDs from sorted entities and convert them to String
            // If entity is a PlayerBase, use getName() instead
            List<String> sortedEntityIDs = distanceMap.values().stream()
                    .map(entity -> {
                        if (entity instanceof PlayerEntity) {
                            return ((PlayerEntity) entity).name;
                        } else {
                            return Integer.toString(entity.id);
                        }
                    })
                    .collect(Collectors.toList());

            // Filter and modify the suggestions based on currentInput
            for (int i = sortedEntityIDs.size() - 1; i >= 0; i--) {  // Change loop condition to i >= 0
                if (!sortedEntityIDs.get(i).startsWith(currentInput)) {
                    sortedEntityIDs.remove(i);
                } else {
                    if (parameterNum == 2)
                    {
                        if (sortedEntityIDs.get(i).equals(totalInput.split(" ")[1]))
                        {
                            sortedEntityIDs.remove(i);
                        }
                        else
                        {
                            sortedEntityIDs.set(i, sortedEntityIDs.get(i).substring(currentInput.length()));
                        }
                    }
                    else
                    {
                        sortedEntityIDs.set(i, sortedEntityIDs.get(i).substring(currentInput.length()));
                    }
                }
            }

            return sortedEntityIDs.toArray(new String[0]);
        }
        return new String[0];
    }

}
