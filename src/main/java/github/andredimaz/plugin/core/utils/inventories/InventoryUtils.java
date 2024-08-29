package github.andredimaz.plugin.core.utils.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    /**
     * Cria um inventário com o tamanho e o título especificados.
     *
     * @param size  Tamanho do inventário (deve ser múltiplo de 9).
     * @param title Título do inventário.
     * @return Um objeto Inventory personalizado.
     */
    public static Inventory createInventory(int size, String title) {
        if (size % 9 != 0 || size > 54) {
            throw new IllegalArgumentException("O tamanho do inventário deve ser um múltiplo de 9 e menor ou igual a 54.");
        }
        return Bukkit.createInventory(null, size, title);
    }

    /**
     * Cria um inventário associado a um InventoryHolder, com o tamanho e o título especificados.
     *
     * @param holder O holder associado ao inventário.
     * @param size   Tamanho do inventário (deve ser múltiplo de 9).
     * @param title  Título do inventário.
     * @return Um objeto Inventory personalizado.
     */
    public static Inventory createInventory(InventoryHolder holder, int size, String title) {
        if (size % 9 != 0 || size > 54) {
            throw new IllegalArgumentException("O tamanho do inventário deve ser um múltiplo de 9 e menor ou igual a 54.");
        }
        return Bukkit.createInventory(holder, size, title);
    }

    /**
     * Adiciona um item a uma posição específica do inventário.
     *
     * @param inventory O inventário onde o item será adicionado.
     * @param slot      O slot onde o item será colocado.
     * @param item      O item a ser adicionado.
     */
    public static void setItem(Inventory inventory, int slot, ItemStack item) {
        if (slot < 0 || slot >= inventory.getSize()) {
            throw new IllegalArgumentException("O slot especificado está fora dos limites do inventário.");
        }
        inventory.setItem(slot, item);
    }

    /**
     * Preenche o inventário inteiro com um item específico.
     *
     * @param inventory O inventário a ser preenchido.
     * @param item      O item que será usado para preencher.
     */
    public static void fillInventory(Inventory inventory, ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, item);
        }
    }

    /**
     * Abre um novo inventário e restaura o item no cursor do jogador se houver.
     *
     * @param player        O jogador que terá o inventário aberto.
     * @param newInventory  O novo inventário a ser aberto.
     */
    public static void openInventoryWithCursorPosition(Player player, Inventory newInventory) {
        // Obtém a visualização atual do inventário do jogador
        InventoryView currentView = player.getOpenInventory();

        // Salva o item atual no cursor
        ItemStack cursorItem = currentView.getCursor();

        // Abre o novo inventário
        player.openInventory(newInventory);

        // Restaura o item no cursor após abrir o novo inventário
        if (cursorItem != null) {
            player.setItemOnCursor(cursorItem);
        }
    }
}
