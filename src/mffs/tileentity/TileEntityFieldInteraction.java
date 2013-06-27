package mffs.tileentity;

import java.util.Set;

import mffs.ModularForceFieldSystem;
import mffs.Settings;
import mffs.api.IFieldInteraction;
import mffs.api.modules.IModule;
import mffs.api.modules.IProjectorMode;
import mffs.base.TileEntityModuleAcceptor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import calclavia.lib.CalculationHelper;

public abstract class TileEntityFieldInteraction extends TileEntityModuleAcceptor implements IFieldInteraction
{
	protected static final int MODULE_SLOT_ID = 2;

	@Override
	public ItemStack getModeStack()
	{
		if (this.getStackInSlot(MODULE_SLOT_ID) != null)
		{
			if (this.getStackInSlot(MODULE_SLOT_ID).getItem() instanceof IProjectorMode)
			{
				return this.getStackInSlot(MODULE_SLOT_ID);
			}
		}

		return null;
	}

	@Override
	public IProjectorMode getMode()
	{
		if (this.getModeStack() != null)
		{
			return (IProjectorMode) this.getModeStack().getItem();
		}

		return null;
	}

	@Override
	public int getSidedModuleCount(IModule module, ForgeDirection... direction)
	{
		int count = 0;

		if (direction != null && direction.length > 0)
		{
			for (ForgeDirection checkDir : direction)
			{
				count += this.getModuleCount(module, this.getSlotsBasedOnDirection(checkDir));
			}
		}
		else
		{
			for (int i = 0; i < 6; i++)
			{
				ForgeDirection checkDir = ForgeDirection.getOrientation(i);
				count += this.getModuleCount(module, this.getSlotsBasedOnDirection(checkDir));
			}
		}

		return count;
	}

	@Override
	public int[] getModuleSlots()
	{
		return new int[] { 15, 16, 17, 18, 19, 20 };
	}

	@Override
	public Vector3 getTranslation()
	{
		String cacheID = "getTranslation";

		if (Settings.USE_CACHE)
		{
			if (this.cache.containsKey(cacheID))
			{
				if (this.cache.get(cacheID) instanceof Vector3)
				{
					return (Vector3) this.cache.get(cacheID);
				}
			}
		}

		ForgeDirection direction = this.getDirection(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

		int zTranslationNeg = this.getModuleCount(ModularForceFieldSystem.itemModuleTranslate, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zTranslationPos = this.getModuleCount(ModularForceFieldSystem.itemModuleTranslate, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xTranslationNeg = this.getModuleCount(ModularForceFieldSystem.itemModuleTranslate, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xTranslationPos = this.getModuleCount(ModularForceFieldSystem.itemModuleTranslate, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yTranslationPos = this.getModuleCount(ModularForceFieldSystem.itemModuleTranslate, this.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yTranslationNeg = this.getModuleCount(ModularForceFieldSystem.itemModuleTranslate, this.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		Vector3 translation = new Vector3(xTranslationPos - xTranslationNeg, yTranslationPos - yTranslationNeg, zTranslationPos - zTranslationNeg);

		if (Settings.USE_CACHE)
		{
			this.cache.put(cacheID, translation);
		}

		return translation;
	}

	@Override
	public Vector3 getPositiveScale()
	{
		String cacheID = "getPositiveScale";

		if (Settings.USE_CACHE)
		{
			if (this.cache.containsKey(cacheID))
			{
				if (this.cache.get(cacheID) instanceof Vector3)
				{
					return (Vector3) this.cache.get(cacheID);
				}
			}
		}

		ForgeDirection direction = this.getDirection(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

		int zScalePos = this.getModuleCount(ModularForceFieldSystem.itemModuleScale, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));
		int xScalePos = this.getModuleCount(ModularForceFieldSystem.itemModuleScale, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));
		int yScalePos = this.getModuleCount(ModularForceFieldSystem.itemModuleScale, this.getSlotsBasedOnDirection(ForgeDirection.UP));

		int omnidirectionalScale = this.getModuleCount(ModularForceFieldSystem.itemModuleScale, this.getModuleSlots());

		zScalePos += omnidirectionalScale;
		xScalePos += omnidirectionalScale;
		yScalePos += omnidirectionalScale;

		Vector3 positiveScale = new Vector3(xScalePos, yScalePos, zScalePos);

		if (Settings.USE_CACHE)
		{
			this.cache.put(cacheID, positiveScale);
		}

		return positiveScale;
	}

	@Override
	public Vector3 getNegativeScale()
	{
		String cacheID = "getNegativeScale";

		if (Settings.USE_CACHE)
		{
			if (this.cache.containsKey(cacheID))
			{
				if (this.cache.get(cacheID) instanceof Vector3)
				{
					return (Vector3) this.cache.get(cacheID);
				}
			}
		}

		ForgeDirection direction = this.getDirection(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

		int zScaleNeg = this.getModuleCount(ModularForceFieldSystem.itemModuleScale, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int xScaleNeg = this.getModuleCount(ModularForceFieldSystem.itemModuleScale, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int yScaleNeg = this.getModuleCount(ModularForceFieldSystem.itemModuleScale, this.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		int omnidirectionalScale = this.getModuleCount(ModularForceFieldSystem.itemModuleScale, this.getModuleSlots());

		zScaleNeg += omnidirectionalScale;
		xScaleNeg += omnidirectionalScale;
		yScaleNeg += omnidirectionalScale;

		Vector3 negativeScale = new Vector3(xScaleNeg, yScaleNeg, zScaleNeg);

		if (Settings.USE_CACHE)
		{
			this.cache.put(cacheID, negativeScale);
		}

		return negativeScale;
	}

	@Override
	public int getRotationYaw()
	{
		String cacheID = "getRotationYaw";

		if (Settings.USE_CACHE)
		{
			if (this.cache.containsKey(cacheID))
			{
				if (this.cache.get(cacheID) instanceof Integer)
				{
					return (Integer) this.cache.get(cacheID);
				}
			}
		}

		ForgeDirection direction = this.getDirection(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		int horizontalRotation = this.getModuleCount(ModularForceFieldSystem.itemModuleRotate, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST))) - this.getModuleCount(ModularForceFieldSystem.itemModuleRotate, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST))) + this.getModuleCount(ModularForceFieldSystem.itemModuleRotate, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH))) - this.getModuleCount(ModularForceFieldSystem.itemModuleRotate, this.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));

		if (Settings.USE_CACHE)
		{
			this.cache.put(cacheID, horizontalRotation);
		}

		return horizontalRotation;
	}

	@Override
	public int getRotationPitch()
	{
		String cacheID = "getRotationPitch";

		if (Settings.USE_CACHE)
		{
			if (this.cache.containsKey(cacheID))
			{
				if (this.cache.get(cacheID) instanceof Integer)
				{
					return (Integer) this.cache.get(cacheID);
				}
			}
		}

		int verticleRotation = this.getModuleCount(ModularForceFieldSystem.itemModuleRotate, this.getSlotsBasedOnDirection(ForgeDirection.UP)) - this.getModuleCount(ModularForceFieldSystem.itemModuleRotate, this.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		if (Settings.USE_CACHE)
		{
			this.cache.put(cacheID, verticleRotation);
		}

		return verticleRotation;
	}

	@Override
	public Set<Vector3> getInteriorPoints()
	{
		String cacheID = "getInteriorPoints";

		if (Settings.USE_CACHE)
		{
			if (this.cache.containsKey(cacheID))
			{
				if (this.cache.get(cacheID) instanceof Set)
				{
					return (Set<Vector3>) this.cache.get(cacheID);
				}
			}
		}

		Set<Vector3> newField = this.getMode().getInteriorPoints(this);

		Vector3 translation = this.getTranslation();
		int rotationYaw = this.getRotationYaw();
		int rotationPitch = this.getRotationPitch();

		for (Vector3 position : newField)
		{
			if (rotationYaw != 0 || rotationPitch != 0)
			{
				CalculationHelper.rotateByAngle(position, rotationYaw, rotationPitch);
			}

			position.add(new Vector3(this));
			position.add(translation);
		}

		if (Settings.USE_CACHE)
		{
			this.cache.put(cacheID, newField);
		}

		return newField;
	}

	@Override
	public int[] getSlotsBasedOnDirection(ForgeDirection direction)
	{
		switch (direction)
		{
			default:
				return new int[] {};
			case UP:
				return new int[] { 3, 11 };
			case DOWN:
				return new int[] { 6, 14 };
			case NORTH:
				return new int[] { 8, 10 };
			case SOUTH:
				return new int[] { 7, 9 };
			case WEST:
				return new int[] { 4, 5 };
			case EAST:
				return new int[] { 12, 13 };
		}
	}
}
