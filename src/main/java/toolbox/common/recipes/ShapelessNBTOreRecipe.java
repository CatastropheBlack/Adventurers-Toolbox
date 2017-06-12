package toolbox.common.recipes;

import java.util.Iterator;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessNBTOreRecipe extends ShapelessOreRecipe {

	public ShapelessNBTOreRecipe(ItemStack result, Object[] recipe) {
		super(result, recipe);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean matches(InventoryCrafting var1, World world) {
		NonNullList<Object> required = NonNullList.create();
		required.addAll(input);

		for (int x = 0; x < var1.getSizeInventory(); x++) {
			ItemStack slot = var1.getStackInSlot(x);

			if (!slot.isEmpty()) {
				boolean inRecipe = false;
				Iterator<Object> req = required.iterator();

				while (req.hasNext()) {
					boolean match = false;

					Object next = req.next();

					if (next instanceof ItemStack) {
						match = OreDictionary.itemMatches((ItemStack) next, slot, false)
								&& ItemStack.areItemStackTagsEqual((ItemStack) next, slot);
					} else if (next instanceof List) {
						Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
						while (itr.hasNext() && !match) {
							match = OreDictionary.itemMatches(itr.next(), slot, false);
						}
					}

					if (match) {
						inRecipe = true;
						required.remove(next);
						break;
					}
				}

				if (!inRecipe) {
					return false;
				}
			}
		}

		return required.isEmpty();
	}

}
