package net.vulpixass.aerocali.content.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.element.InputWindowElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.block.custom.cable.CableBlock;
import net.vulpixass.aerocali.content.block.custom.thruster.ThrusterBlockEntity;
import net.vulpixass.aerocali.content.item.AerocaliItems;

import java.util.List;

public class PonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> registry) {
        PonderSceneRegistrationHelper<DeferredHolder<?, ?>> helper = registry.withKeyFunction(DeferredHolder::getId);
        ResourceLocation aerocaliGroupId = ResourceLocation.fromNamespaceAndPath("aerocali", "aerocali");

        helper.forComponents(AerocaliBlocks.THRUSTER)
                .addStoryBoard("thruster_functionality", PonderScenes::thrusterFunctionality, aerocaliGroupId);

        helper.forComponents(AerocaliItems.IONIZED_THERMAL_MECHANISM)
                .addStoryBoard("thruster_upgrading", PonderScenes::thrusterUpgrading, aerocaliGroupId);

        helper.forComponents(AerocaliBlocks.GENERATOR, AerocaliBlocks.INDUSTRIAL_GENERATOR, AerocaliBlocks.CREATIVE_GENERATOR)
                .addStoryBoard("generator_functionality", PonderScenes::generatorFunctionality, aerocaliGroupId);

        helper.forComponents(AerocaliBlocks.TRACKER)
                .addStoryBoard("tracker_functionality", PonderScenes::trackerFunctionality, aerocaliGroupId);

        helper.forComponents(AerocaliBlocks.CABLE)
                .addStoryBoard("cable_functionality", PonderScenes::cableFunctionality, aerocaliGroupId);

        helper.forComponents(AerocaliBlocks.MECHANICAL_ANVIL)
                .addStoryBoard("anvil_functionality", PonderScenes::anvilFunctionality, aerocaliGroupId);
    }

    // Mechanical Anvil Functionality Scene
    public static void anvilFunctionality(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("anvil_functionality", "How the Mechanical Anvil works");

        scene.configureBasePlate(0, 0, 5);
        scene.idle(15);
        scene.world().showSection(util.select().everywhere(), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(30).text("This is the Mechanical Anvil.")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(35);

        scene.overlay().showText(45).text("Depending on the RPM inputted...")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(40);
        scene.world().setKineticSpeed(util.select().everywhere(), 16.0f);
        scene.idle(5);

        scene.overlay().showText(60).text("The Anvil will change its Mass!")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(65);

        scene.world().setKineticSpeed(util.select().everywhere(), 0f);
        scene.overlay().showText(30).text("The Anvil can change its mass to 4 Values:")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(35);

        scene.world().setKineticSpeed(util.select().everywhere(), 16f);
        scene.overlay().showText(30).text("Super Light")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(35);

        scene.world().setKineticSpeed(util.select().everywhere(), 32f);
        scene.overlay().showText(30).text("Light")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(35);

        scene.world().setKineticSpeed(util.select().everywhere(), 64f);
        scene.overlay().showText(30).text("Heavy")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(35);

        scene.world().setKineticSpeed(util.select().everywhere(), 128f);
        scene.overlay().showText(40).text("And Lastly Super Heavy")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(45);
    }

    // Cable Functionality Scene
    public static void cableFunctionality(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        BlockPos cablePos = new BlockPos(2, 1, 2);

        scene.title("cable_functionality", "How the Electron Cable works");

        scene.configureBasePlate(0, 0, 5);
        scene.idle(15);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.world().showSection(util.select().position(2, 1, 2), Direction.UP);
        scene.world().modifyBlock(cablePos, state -> {
            if (state.hasProperty(CableBlock.NORTH) && state.hasProperty(CableBlock.SOUTH) && state.hasProperty(CableBlock.EAST) && state.hasProperty(CableBlock.WEST))
                return state.setValue(CableBlock.NORTH, false).setValue(CableBlock.SOUTH, false).setValue(CableBlock.EAST, false)
                        .setValue(CableBlock.WEST, false);
            return state;
        }, false);
        scene.idle(10);

        scene.overlay().showText(30).text("This is an Electron Cable.")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(35);

        scene.overlay().showText(60).text("It serves as a way to transfer FE from Point A to B")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(65);

        scene.overlay().showText(40).text("When it does have power...")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(45);

        scene.world().modifyBlock(cablePos, state -> {
            if (state.hasProperty(CableBlock.ACTIVE)) {
                return state.setValue(CableBlock.ACTIVE, true);
            }
            return state;
        }, false);
        scene.effects().indicateSuccess(cablePos);

        scene.overlay().showText(30).text("It turns On!")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(35);

        scene.world().modifyBlock(cablePos, state -> {
            if (state.hasProperty(CableBlock.ACTIVE)) {
                return state.setValue(CableBlock.ACTIVE, false);
            }
            return state;
        }, false);

        scene.world().showSection(util.select().fromTo(4, 1, 2, 3, 1, 2), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(0, 1, 2, 1, 1, 2), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(2, 1, 3, 2, 1, 4), Direction.DOWN);
        scene.world().showSection(util.select().fromTo(2, 1, 0, 2, 1, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().modifyBlock(cablePos, state -> {
            if (state.hasProperty(CableBlock.NORTH) && state.hasProperty(CableBlock.SOUTH) && state.hasProperty(CableBlock.EAST) && state.hasProperty(CableBlock.WEST))
                return state.setValue(CableBlock.NORTH, true).setValue(CableBlock.SOUTH, true).setValue(CableBlock.EAST, true)
                        .setValue(CableBlock.WEST, true);
            return state;
        }, false);

        scene.overlay().showText(60).text("When other Cables are next to it, it transfers Power")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(65);
    }

    // Tracker Functionality Scene
    public static void trackerFunctionality(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        BlockPos trackerPos = new BlockPos(3, 2, 2);
        BlockPos trackerWirePos = new BlockPos(4, 2, 2);
        BlockPos tableWirePos = new BlockPos(3, 1, 3);
        Vec3 trackerVector3 = util.vector().blockSurface(trackerPos, Direction.UP);

        scene.title("tracker_functionality", "How the Navigation Tracker works");
        scene.rotateCameraY(180);
        scene.configureBasePlate(1, 0, 5);
        scene.idle(15);
        scene.world().setKineticSpeed(util.select().everywhere(), 32.0f);
        scene.world().showSection(util.select().everywhere(), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(30).text("This is the Navigation Tracker.")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.UP)).placeNearTarget();
        scene.idle(35);
        scene.overlay().showText(40).text("It requires a shaft above with an RPM speed of at least 30")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 3, 2), Direction.UP)).placeNearTarget();
        scene.overlay().showText(40).text("And a Navigation Table below to function.")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 1, 2), Direction.DOWN)).placeNearTarget();
        scene.idle(45);

        scene.overlay().showText(60).text("When a Navigation Compass is inputted, the Tracker will output a redstone signal depending on how far away it is from the target.")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.UP)).placeNearTarget();
        scene.idle(50);
        scene.overlay().showControls(trackerVector3, Pointing.DOWN, 10).rightClick().withItem(AerocaliItems.NAVIGATION_ELEMENT.toStack());
        scene.idle(4);

        // changes Active state of Ponder Blocks
        scene.world().modifyBlock(trackerWirePos, state -> {
            if (state.hasProperty(RedStoneWireBlock.POWER)) {
                return state.setValue(RedStoneWireBlock.POWER, 7);
            }
            return state;
        }, false);
        scene.world().modifyBlock(trackerWirePos.offset(1, 0, 0), state -> {
            if (state.hasProperty(RedstoneLampBlock.LIT)) {
                return state.setValue(RedstoneLampBlock.LIT, true);
            }
            return state;
        }, false);
        scene.idle(2);
        scene.world().modifyBlock(tableWirePos, state -> {
            if (state.hasProperty(RedStoneWireBlock.POWER)) {
                return state.setValue(RedStoneWireBlock.POWER, 9);
            }
            return state;
        }, false);
        scene.world().modifyBlock(tableWirePos.offset(0, 0, 1), state -> {
            if (state.hasProperty(RedstoneLampBlock.LIT)) {
                return state.setValue(RedstoneLampBlock.LIT, true);
            }
            return state;
        }, false);
        scene.idle(10);
        scene.overlay().showText(40).text("The Tracker will also pass the compass immediately to the Navigation Table below.")
                .pointAt(util.vector().blockSurface(util.grid().at(3, 1, 3), Direction.UP)).placeNearTarget();
        scene.idle(40);

    }

    // Generator Functionality Scene
    public static void generatorFunctionality(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("generator_functionality", "How the generator works");
        scene.rotateCameraY(180);
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(5);

        scene.world().setKineticSpeed(util.select().everywhere(), 0.0f);
        scene.world().showSection(util.select().position(util.grid().at(2, 1, 2)), Direction.DOWN);
        scene.idle(5);

        scene.overlay().showText(30).text("This is the Generator.")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(50);

        scene.world().showSection(util.select().cuboid(util.grid().at(2, 1, 0), util.grid().at(3, 2, 1)), Direction.SOUTH);
        scene.idle(30);

        scene.world().setKineticSpeed(util.select().everywhere(), -16.0f);
        scene.overlay().showText(30).text("When Powered by RPM it will generate FE")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();
        scene.idle(60);

        scene.world().setKineticSpeed(util.select().everywhere(), -32.0f);
        scene.overlay().showText(60).text("When the RPM is faster, the Generator will generate more FE/tick with a cap of 15 FE/tick")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 1, 2), Direction.UP)).placeNearTarget();

        scene.idle(80);

    }

    // Thruster Upgrading Scene
    public static void thrusterUpgrading(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        BlockPos leverPos = util.grid().at(5, 2, 2);
        BlockPos wirePos = util.grid().at(4, 2, 2);
        BlockPos thrusterPos = util.grid().at(3, 2, 2);
        Vec3 thrusterVector3 = util.vector().blockSurface(thrusterPos, Direction.UP);

        scene.title("thruster_upgrading", "How to upgrade a Thruster");
        scene.rotateCameraY(180);
        scene.configureBasePlate(1, 0, 5);
        scene.showBasePlate();
        scene.world().setKineticSpeed(util.select().everywhere(), 32.0f);
        scene.world().showSection(util.select().everywhere(), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(40).text("Normally the Thruster outputs fire particles with an okay amount of thrust and power consumption.")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.UP)).placeNearTarget();


        scene.world().modifyBlock(leverPos, state -> {
            if (state.hasProperty(LeverBlock.POWERED)) {
                return state.setValue(LeverBlock.POWERED, true);
            }
            return state;
        }, false);

        scene.idle(2);
        scene.world().modifyBlock(wirePos, state -> {
            if (state.hasProperty(RedStoneWireBlock.POWER)) {
                return state.setValue(RedStoneWireBlock.POWER, 14);
            }
            return state;
        }, false);
        scene.idle(2);
        scene.world().modifyBlock(thrusterPos, state -> {
            if (state.hasProperty(BlockStateProperties.POWERED)) {
                return state.setValue(BlockStateProperties.POWERED, true);
            }
            return state;
        }, false);

        // Spawns Particles of the Thruster
        for (int t = 0; t < 40; t++) {
            scene.world().modifyBlockEntity(thrusterPos, ThrusterBlockEntity.class, thruster -> {
                thruster.thrust = 5.0f;
                thruster.ion = false;

                thruster.spawnFlameParticles();
            });
            scene.idle(1);
        }
        scene.world().modifyBlock(leverPos, state -> {
            if (state.hasProperty(LeverBlock.POWERED)) {
                return state.setValue(LeverBlock.POWERED, false);
            }
            return state;
        }, false);

        scene.idle(2);
        scene.world().modifyBlock(wirePos, state -> {
            if (state.hasProperty(RedStoneWireBlock.POWER)) {
                return state.setValue(RedStoneWireBlock.POWER, 0);
            }
            return state;
        }, false);
        scene.idle(2);
        scene.world().modifyBlock(thrusterPos, state -> {
            if (state.hasProperty(BlockStateProperties.POWERED)) {
                return state.setValue(BlockStateProperties.POWERED, false);
            }
            return state;
        }, false);

        scene.idle(25);

        scene.overlay().showText(30).text("By right clicking on it with an Ionized Thermal Mechanism...")
                .attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.UP)).placeNearTarget();
        scene.idle(10);
        scene.overlay().showControls(thrusterVector3, Pointing.DOWN, 10).rightClick().withItem(AerocaliItems.IONIZED_THERMAL_MECHANISM.toStack());
        scene.idle(50);

        scene.world().modifyBlock(leverPos, state -> {
            if (state.hasProperty(LeverBlock.POWERED)) {
                return state.setValue(LeverBlock.POWERED, true);
            }
            return state;
        }, false);

        scene.idle(2);
        scene.world().modifyBlock(wirePos, state -> {
            if (state.hasProperty(RedStoneWireBlock.POWER)) {
                return state.setValue(RedStoneWireBlock.POWER, 14);
            }
            return state;
        }, false);
        scene.idle(2);
        scene.world().modifyBlock(thrusterPos, state -> {
            if (state.hasProperty(BlockStateProperties.POWERED)) {
                return state.setValue(BlockStateProperties.POWERED, true);
            }
            return state;
        }, false);

        for (int t = 0; t < 20; t++) {
            scene.world().modifyBlockEntity(thrusterPos, ThrusterBlockEntity.class, thruster -> {
                thruster.thrust = 5.0f;
                thruster.ion = true;

                thruster.spawnFlameParticles();
            });
            scene.idle(1);
        }
        scene.overlay().showText(40).text("The Particles will become soul fire particles and the Thruster will require twice as much thrust and power " +
                "consumption than normally").pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.UP)).placeNearTarget();
        scene.idle(30);
    }

    // Thruster Functionality Scene
    public static void thrusterFunctionality(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        BlockPos leverPos = util.grid().at(5, 2, 2);
        BlockPos wirePos = util.grid().at(4, 2, 2);
        BlockPos thrusterPos = util.grid().at(3, 2, 2);
        Vec3 controlPos = util.vector().blockSurface(leverPos, Direction.UP);

        scene.title("thruster_functionality", "How the Thruster works");
        scene.rotateCameraY(180);
        scene.configureBasePlate(1, 0, 5);
        scene.showBasePlate();
        scene.idle(15);
        scene.world().setKineticSpeed(util.select().everywhere(), 32.0f);

        scene.world().showSection(util.select().cuboid(util.grid().at(1, 1, 0), util.grid().at(5, 0, 5)), Direction.UP);
        scene.idle(2);
        scene.world().showSection(util.select().position(util.grid().at(3, 2, 2)), Direction.DOWN);
        scene.idle(10);
        scene.overlay().showText(60).text("This is the main Thruster unit.").attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2),
                Direction.UP)).placeNearTarget();
        scene.idle(60);
        scene.world().showSection(util.select().cuboid(util.grid().at(0, 0, 0), util.grid().at(3, 5, 1)), Direction.UP);
        scene.idle(30);
        scene.overlay().showText(60).text("By Powering it using a generator it gains FE.").attachKeyFrame().pointAt(util.vector().blockSurface(util.grid()
                .at(3, 2, 1), Direction.UP)).placeNearTarget();
        scene.idle(90);
        scene.world().showSection(util.select().cuboid(util.grid().at(4, 2, 2), util.grid().at(5, 2, 2)), Direction.DOWN);
        scene.idle(15);
        scene.overlay().showText(15).text("When getting a redstone signal inputted...").attachKeyFrame().pointAt(util.vector().blockSurface(util.grid()
                .at(5, 2, 2), Direction.UP)).placeNearTarget();
        scene.idle(13);
        scene.overlay().showControls(controlPos, Pointing.DOWN, 30).rightClick().withItem(Items.AIR.getDefaultInstance());
        scene.idle(10);
        scene.world().modifyBlock(leverPos, state -> {
            if (state.hasProperty(LeverBlock.POWERED)) {
                return state.setValue(LeverBlock.POWERED, true);
            }
            return state;
        }, false);

        scene.idle(2);
        scene.world().modifyBlock(wirePos, state -> {
            if (state.hasProperty(RedStoneWireBlock.POWER)) {
                return state.setValue(RedStoneWireBlock.POWER, 14);
            }
            return state;
        }, false);
        scene.idle(2);
        scene.world().modifyBlock(thrusterPos, state -> {
            if (state.hasProperty(BlockStateProperties.POWERED)) {
                return state.setValue(BlockStateProperties.POWERED, true);
            }
            return state;
        }, false);

        // Spawns Particles of the Thruster
        for (int t = 0; t < 40; t++) {
            scene.world().modifyBlockEntity(thrusterPos, ThrusterBlockEntity.class, thruster -> {
                thruster.thrust = 5.0f;
                thruster.ion = false;

                thruster.spawnFlameParticles();
            });
            scene.idle(1);
        }
        scene.idle(20);
        scene.overlay().showText(20).text("The Thruster turns on!").pointAt(util.vector().blockSurface(util.grid().at(3, 2, 2), Direction.UP))
                .placeNearTarget();
        scene.idle(30);
    }
}
