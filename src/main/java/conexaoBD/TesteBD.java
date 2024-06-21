package conexaoBD;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.*;

import classesObjetos.Jogo;

import javax.imageio.ImageIO;
import javax.swing.*;

public class TesteBD {
    private static String URL = "jdbc:sqlite:src/main/resources/testando.bd";
    private static String usuario = "root";
    private static String senha = "";



    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void cadastrarJogo(Jogo jogo) {
        String insertJogo = "INSERT INTO jogos(id_jogo, titulo_jogo, genero_jogo, preco_jogo, descricao_jogo, imagem_jogo)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stm = conn.prepareStatement(insertJogo)) {
            stm.setInt(1, jogo.getIdJogo());
            stm.setString(2, jogo.getTitulo());
            stm.setString(3, jogo.getGenero());
            stm.setDouble(4, jogo.getPreco());
            stm.setString(5, jogo.getDescricao());
            stm.setBytes(6, jogo.getImagem());

            stm.executeUpdate();
            System.out.println("Jogo " + jogo.getTitulo() + " adicionado ao banco de dados.");
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Erro de conexão ao BD", "ERRO SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static List<Jogo> getJogosCadastrados() {
        List<Jogo> jogos = new ArrayList<>();
        String selectQuery = "SELECT id_jogo, titulo_jogo, genero_jogo, preco_jogo, descricao_jogo FROM jogos";

        try (Connection conn = getConnection();
             PreparedStatement stm = conn.prepareStatement(selectQuery);
             ResultSet rs = stm.executeQuery()) {

            while(rs.next()) {
                int idJogo = rs.getInt("id_jogo");
                String titulo = rs.getString("titulo_jogo");
                String genero = rs.getString("genero_jogo");
                double preco = rs.getDouble("preco_jogo");
                String descricao = rs.getString("descricao_jogo");
                byte[] imagem = rs.getBytes("imagem_jogo");
//                Blob blobimg = (Blob) rs.getBlob("imagem_jogo");
//                byte[] img = blobimg.getBytes(1, (int) blobimg.length());  // converte os dados binários para arquivo de imagem
////                BufferedImage imagem = null;
                try {
//                    imagem = ImageIO.read(new ByteArrayInputStream(img));
                } catch(Exception e) {
                    System.out.println(e);
                }

                Jogo jogo = new Jogo(idJogo,genero, titulo, preco, descricao, imagem); // Supondo que o construtor de Jogo aceite genero e titulo
                jogos.add(jogo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Jogos recuperados do banco de dados: " + jogos.size());
        for (Jogo jogo : jogos) {
            System.out.println("ID: " + jogo.getIdJogo() + ", Título: " + jogo.getTitulo() + ", Genero: " + jogo.getGenero() +
                    ", Preço: " + jogo.getPreco() + ", Descrição: " + jogo.getDescricao());
        }

        return jogos;
    }
     public static void main(String[] args) {
        getConnection();
    }
}
