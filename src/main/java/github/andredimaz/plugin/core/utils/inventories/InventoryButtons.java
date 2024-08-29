package github.andredimaz.plugin.core.utils.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InventoryButtons {

    private final Inventory inventory;
    private final List<Button> buttons;

    /**
     * Construtor para criar um inventário com botões.
     *
     * @param size  Tamanho do inventário.
     * @param title Título do inventário.
     */
    public InventoryButtons(int size, String title) {
        this.inventory = InventoryUtils.createInventory(size, title);
        this.buttons = new ArrayList<>();
    }

    /**
     * Classe interna para representar um botão no inventário.
     */
    public static class Button {
        private final org.bukkit.inventory.ItemStack item;
        private final int[] slots;
        private final Consumer<Player> action;

        public Button(String materialString, String name, List<String> lore, int[] slots, Consumer<Player> action) {
            this.item = MaterialUtils.parseMaterial(materialString);
            org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(name);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            this.slots = slots;
            this.action = action;
        }

        public void placeInInventory(Inventory inventory) {
            for (int slot : slots) {
                if (slot >= 0 && slot < inventory.getSize()) {
                    inventory.setItem(slot, item);
                }
            }
        }

        public void onClick(Player player) {
            if (action != null) {
                action.accept(player);
            }
        }

        public int[] getSlots() {
            return slots;
        }
    }

    /**
     * Adiciona um botão ao inventário.
     *
     * @param button O botão a ser adicionado.
     */
    public void addButton(Button button) {
        button.placeInInventory(inventory);
        buttons.add(button);
    }

    /**
     * Processa o clique em um botão do inventário.
     *
     * @param event O evento de clique no inventário.
     */
    public void handleClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true); // Prevenir o movimento de itens
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                for (Button button : buttons) {
                    for (int slot : button.getSlots()) {
                        if (event.getSlot() == slot) {
                            button.onClick(player);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Retorna o inventário associado a este InventoryButtons.
     *
     * @return O inventário.
     */
    public Inventory getInventory() {
        return inventory;
    }
}
