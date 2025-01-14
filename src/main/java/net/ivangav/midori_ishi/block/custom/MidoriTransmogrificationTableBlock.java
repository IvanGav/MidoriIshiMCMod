package net.ivangav.midori_ishi.block.custom;

import net.ivangav.midori_ishi.block.entity.MidoriTransmogrificationTableEntity;
import net.ivangav.midori_ishi.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class MidoriTransmogrificationTableBlock extends BaseEntityBlock {
//    protected static final VoxelShape SHAPE = Shapes.or(Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D), Block.box(7.0D, 0.0D, 7.0D, 9.0D, 14.0D, 9.0D));

    public MidoriTransmogrificationTableBlock(Properties pProperties) {
        super(pProperties);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MidoriTransmogrificationTableEntity(pPos, pState);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(
                pBlockEntityType,
                ModBlockEntities.MIDORI_TRANSMOGRIFICATION_TABLE_ENTITY.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1)
        );
    }

//    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
//        return SHAPE;
//    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof MidoriTransmogrificationTableEntity) {
//                pPlayer.openMenu((MidoriInfusionTableEntity)blockentity); //i wish this worked... it doesn't
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (MidoriTransmogrificationTableEntity) blockentity, pPos);
            }
            return InteractionResult.CONSUME;
        }
    }

//    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
//        if (pStack.hasCustomHoverName()) {
//            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
//            if (blockentity instanceof MidoriTransmogrificationTableEntity) {
//                ((MidoriTransmogrificationTableEntity)blockentity).setCustomName(pStack.getHoverName());
//            }
//        }
//    }

//    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
//        double d0 = (double)pPos.getX() + 0.4D + (double)pRandom.nextFloat() * 0.2D;
//        double d1 = (double)pPos.getY() + 0.7D + (double)pRandom.nextFloat() * 0.3D;
//        double d2 = (double)pPos.getZ() + 0.4D + (double)pRandom.nextFloat() * 0.2D;
//        pLevel.addParticle(ParticleTypes.CRIT, d0, d1, d2, 0.0D, 0.0D, 0.0D);
//    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof MidoriTransmogrificationTableEntity) {
                ((MidoriTransmogrificationTableEntity)blockentity).drops();
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

//    public boolean hasAnalogOutputSignal(BlockState pState) {
//        return true;
//    }
//
//    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
//        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(pLevel.getBlockEntity(pPos));
//    }

//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
//        pBuilder.add(HAS_BOTTLE[0], HAS_BOTTLE[1], HAS_BOTTLE[2]);
//    }

    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }
}
