package github.andredimaz.plugin.core.utils.basics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * Cria um arquivo se ele não existir.
     *
     * @param file O arquivo a ser criado.
     * @return `true` se o arquivo foi criado, `false` se já existia.
     * @throws IOException Se ocorrer um erro ao criar o arquivo.
     */
    public static boolean createFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            return file.createNewFile();
        }
        return false;
    }

    /**
     * Cria um diretório se ele não existir.
     *
     * @param directory O diretório a ser criado.
     * @return `true` se o diretório foi criado, `false` se já existia.
     */
    public static boolean createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return false;
    }

    /**
     * Escreve uma lista de strings em um arquivo, sobrescrevendo o conteúdo existente.
     *
     * @param file   O arquivo onde o conteúdo será escrito.
     * @param lines  As linhas de texto a serem escritas no arquivo.
     * @throws IOException Se ocorrer um erro ao escrever no arquivo.
     */
    public static void writeLinesToFile(File file, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Lê todas as linhas de um arquivo e as retorna como uma lista de strings.
     *
     * @param file O arquivo a ser lido.
     * @return Uma lista contendo as linhas do arquivo.
     * @throws IOException Se ocorrer um erro ao ler o arquivo.
     */
    public static List<String> readLinesFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Verifica se um arquivo ou diretório existe.
     *
     * @param path O caminho do arquivo ou diretório.
     * @return `true` se o arquivo ou diretório existir, `false` caso contrário.
     */
    public static boolean exists(File path) {
        return path.exists();
    }

    /**
     * Exclui um arquivo ou diretório.
     * Se for um diretório, exclui recursivamente todos os arquivos e subdiretórios.
     *
     * @param path O caminho do arquivo ou diretório a ser excluído.
     * @return `true` se a exclusão foi bem-sucedida, `false` caso contrário.
     */
    public static boolean deleteFileOrDirectory(File path) {
        if (path.isDirectory()) {
            for (File sub : path.listFiles()) {
                deleteFileOrDirectory(sub);
            }
        }
        return path.delete();
    }

    /**
     * Copia o conteúdo de um arquivo para outro.
     *
     * @param source      O arquivo de origem.
     * @param destination O arquivo de destino.
     * @throws IOException Se ocorrer um erro durante a cópia.
     */
    public static void copyFile(File source, File destination) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Movimenta um arquivo para outro local.
     *
     * @param source      O arquivo de origem.
     * @param destination O arquivo de destino.
     * @return `true` se o arquivo foi movido com sucesso, `false` caso contrário.
     * @throws IOException Se ocorrer um erro durante a movimentação.
     */
    public static boolean moveFile(File source, File destination) throws IOException {
        if (source.renameTo(destination)) {
            return true;
        } else {
            copyFile(source, destination);
            return deleteFileOrDirectory(source);
        }
    }
}

