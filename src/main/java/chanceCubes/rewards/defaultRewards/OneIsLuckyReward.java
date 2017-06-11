package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class OneIsLuckyReward implements IChanceCubeReward
{
	@Override
	public void trigger(final World world, final BlockPos pos, EntityPlayer player)
	{
		RewardsUtil.sendMessageToNearPlayers(world, pos, 32, "A Lucky Block Salute");
		TileEntitySign sign = new TileEntitySign();
		sign.signText[0] = new TextComponentString("One is lucky");
		sign.signText[1] = new TextComponentString("One is not");
		sign.signText[3] = new TextComponentString("#OGLuckyBlocks");
		boolean leftLucky = RewardsUtil.rand.nextBoolean();
		TileChanceCube leftCube = new TileChanceCube(leftLucky ? 100 : -100);
		TileChanceCube rightCube = new TileChanceCube(!leftLucky ? 100 : -100);

		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(-1, 0, 0)))
			world.setTileEntity(pos.add(-1, 0, 0), leftCube);
		if(RewardsUtil.placeBlock(Blocks.STANDING_SIGN.getDefaultState(), world, pos))
			world.setTileEntity(pos, sign);
		if(RewardsUtil.placeBlock(CCubesBlocks.CHANCE_CUBE.getDefaultState(), world, pos.add(1, 0, 0)))
			world.setTileEntity(pos.add(1, 0, 0), rightCube);

		Scheduler.scheduleTask(new Task("One_Is_Lucky_Reward", 6000, 10)
		{
			@Override
			public void callback()
			{
				world.setBlockToAir(pos.add(-1, 0, 0));
				world.setBlockToAir(pos);
				world.setBlockToAir(pos.add(1, 0, 0));
			}

			@Override
			public void update()
			{
				if(world.isAirBlock(pos.add(-1, 0, 0)) || world.isAirBlock(pos.add(1, 0, 0)))
				{
					this.callback();
					Scheduler.removeTask(this);
				}
			}
		});
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":One_Is_Lucky";
	}
}