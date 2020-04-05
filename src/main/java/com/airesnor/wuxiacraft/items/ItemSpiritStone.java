package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillAction;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemSpiritStone extends ItemBase {

	private ISkillAction action;
	private ISkillAction whenUsing;
	public final int color;

	public ItemSpiritStone(String item_name, int color) {
		super(item_name);
		this.action = actor -> true;
		this.whenUsing = new DefaultCultivationIncrease(1f);
		this.setMaxDamage(100);
		this.color = color;
	}

	public ItemSpiritStone setAmount(double amount) {
		this.whenUsing = new DefaultCultivationIncrease(amount);
		return this;
	}

	public ItemSpiritStone setUseAction(ISkillAction action) {
		this.action = action;
		return this;
	}

	public ItemSpiritStone setWhenUsing(ISkillAction action) {
		this.whenUsing = action;
		return this;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	@Nonnull
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10;
	}

	@Override
	@Nonnull
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(stack.getItemDamage() < stack.getMaxDamage()) {
			if(this.whenUsing.activate(entityLiving)) {
				stack.damageItem(1, entityLiving);
			}
		}
		else if(this.action.activate(entityLiving)) {
			stack.damageItem(1, entityLiving);
		}
		return stack;
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	public static class DefaultCultivationIncrease implements ISkillAction {
		public double amount;

		public DefaultCultivationIncrease(double amount) {
			this.amount = amount;
		}

		@Override
		public boolean activate(EntityLivingBase actor) {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
			CultivationUtils.cultivatorAddProgress(actor, cultivation, this.amount);
			return true;
		}
	}
}