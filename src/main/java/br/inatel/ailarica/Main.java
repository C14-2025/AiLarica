package br.inatel.ailarica;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuarioService usuarioService = new UsuarioService();

        boolean rodando = true;

        while (rodando) {
            System.out.println("\n=== Menu ===");
            System.out.println("1 - Cadastro");
            System.out.println("2 - Login");
            System.out.println("3 - Atualizar Senha");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) { // Cadastro
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                Usuario usuario = new Usuario(nome, email, senha);
                boolean sucesso = usuarioService.cadastrar(usuario);

                if (sucesso) {
                    // Simulação de envio de código de confirmação
                    int codigo = new Random().nextInt(9000) + 1000;
                    System.out.println("Código de confirmação (simulado): " + codigo);

                    System.out.print("Digite o código recebido: ");
                    int entrada = scanner.nextInt();
                    scanner.nextLine(); // limpar buffer
                    if (entrada == codigo) {
                        usuario.confirmar();
                        // Atualizar no arquivo
                        List<Usuario> usuarios = usuarioService.carregarUsuarios();
                        for (Usuario u : usuarios) {
                            if (u.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                                u.confirmar();
                            }
                        }
                        usuarioService.atualizarUsuarios(usuarios);
                        System.out.println("Email confirmado com sucesso!");
                    } else {
                        System.out.println("Código incorreto. Conta não confirmada.");
                    }
                } else {
                    System.out.println("Email já existe.");
                }

            } else if (opcao == 2) { // Login
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String senha = scanner.nextLine();

                Usuario usuario = usuarioService.login(email, senha);
                if (usuario != null) {
                    System.out.println("Login bem-sucedido. Bem-vindo, " + usuario.getNome() + "!");
                } else {
                    System.out.println("Email, senha incorretos ou conta não confirmada.");
                }

            } else if (opcao == 3) { // Atualizar Senha
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Senha Antiga: ");
                String senhaAntiga = scanner.nextLine();
                System.out.print("Nova Senha: ");
                String novaSenha = scanner.nextLine();

                boolean sucesso = usuarioService.atualizarSenha(email, senhaAntiga, novaSenha);
                if (sucesso) {
                    System.out.println("Senha atualizada com sucesso!");
                } else {
                    System.out.println("Email ou senha antiga incorretos.");
                }

            } else if (opcao == 4) { // Sair
                rodando = false;
                System.out.println("Saindo...");
            } else {
                System.out.println("Opção inválida!");
            }
        }

        scanner.close();
    }
}