import java.util.*;

class Grafo {
    private Map<String, Map<String, Integer>> grafo; // Estrutura de dados para armazenar o grafo de obras literárias

    // Construtor da classe Grafo
    public Grafo() {
        this.grafo = new HashMap<>(); // Inicializa o mapa que representa o grafo
    }

    // Método para adicionar uma conexão entre duas obras literárias com um peso
    public void adicionarConexao(String obraA, String obraB, int peso) {
        // Verifica se as obras já existem no grafo, se não existirem, adiciona-as
        if (!grafo.containsKey(obraA)) {
            grafo.put(obraA, new HashMap<>());
        }
        if (!grafo.containsKey(obraB)) {
            grafo.put(obraB, new HashMap<>());
        }
        // Adiciona a conexão bidirecional entre as obras com o peso especificado
        grafo.get(obraA).put(obraB, peso);
        grafo.get(obraB).put(obraA, peso);
    }

    // Método para recomendar obras com base nos interesses do leitor a partir de
    // uma obra inicial
    public List<String> recomendarObras(String obraInicial, Map<String, Set<String>> preferenciasUsuarios) {
        // Mapa para armazenar as recomendações de obras e seus pesos
        Map<String, Integer> recomendacoes = new HashMap<>();
        // Executa o algoritmo de busca em profundidade (DFS) a partir da obra inicial
        dfs(obraInicial, new HashSet<>(), recomendacoes, preferenciasUsuarios);

        // Converte as recomendações para uma lista e as classifica por peso em ordem
        // decrescente
        List<String> obrasRecomendadas = new ArrayList<>(recomendacoes.keySet());
        obrasRecomendadas.sort((o1, o2) -> recomendacoes.get(o2).compareTo(recomendacoes.get(o1)));

        return obrasRecomendadas; // Retorna a lista de obras recomendadas
    }

    // Método auxiliar para realizar a busca em profundidade (DFS)
    private void dfs(String obraAtual, Set<String> visitados, Map<String, Integer> recomendacoes,
            Map<String, Set<String>> preferenciasUsuarios) {
        visitados.add(obraAtual); // Marca a obra atual como visitada

        // Obtém as conexões da obra atual
        Map<String, Integer> conexoes = grafo.get(obraAtual);
        if (conexoes != null) {
            // Para cada conexão, verifica se ainda não foi visitada
            for (Map.Entry<String, Integer> conexao : conexoes.entrySet()) {
                String obra = conexao.getKey();
                int peso = conexao.getValue();
                if (!visitados.contains(obra)) {
                    // Verifica se a obra é do interesse de algum usuário
                    for (Map.Entry<String, Set<String>> preferencia : preferenciasUsuarios.entrySet()) {
                        String usuario = preferencia.getKey();
                        Set<String> preferencias = preferencia.getValue();
                        if (preferencias.contains(obra)) {
                            // Se a obra é do interesse do leitor, adiciona-a às recomendações com seu peso
                            recomendacoes.put(obra, recomendacoes.getOrDefault(obra, 0) + peso);
                        }
                    }
                    // Continua a busca em profundidade recursivamente
                    dfs(obra, visitados, recomendacoes, preferenciasUsuarios);
                }
            }
        }
    }

    // Método para imprimir o grafo
    public void imprimirGrafo() {
        System.out.println("Mapa do Grafo:");
        for (Map.Entry<String, Map<String, Integer>> entry : grafo.entrySet()) {
            String obra = entry.getKey();
            Map<String, Integer> conexoes = entry.getValue();
            System.out.print(obra + " -> ");
            for (Map.Entry<String, Integer> conexao : conexoes.entrySet()) {
                System.out.print(conexao.getKey() + "(" + conexao.getValue() + ") ");
            }
            System.out.println();
        }
    }
}

public class RecomendacaoObrasLiterarias {
    public static void main(String[] args) {
        // Criando o grafo de obras literárias com pesos nas conexões
        Grafo grafoObras = new Grafo();
        grafoObras.adicionarConexao("Dom Quixote", "Cem Anos de Solidão", 3);
        grafoObras.adicionarConexao("Dom Quixote", "Moby Dick", 2);
        grafoObras.adicionarConexao("Cem Anos de Solidão", "O Grande Gatsby", 4);
        grafoObras.adicionarConexao("Cem Anos de Solidão", "Orgulho e Preconceito", 3);
        grafoObras.adicionarConexao("Moby Dick", "Hamlet", 2);
        grafoObras.adicionarConexao("O Grande Gatsby", "Orgulho e Preconceito", 5);
        grafoObras.adicionarConexao("O Grande Gatsby", "Hamlet", 3);
        grafoObras.adicionarConexao("Hamlet", "Dom Quixote", 1);
        grafoObras.adicionarConexao("Orgulho e Preconceito", "Moby Dick", 1);
        grafoObras.adicionarConexao("Orgulho e Preconceito", "Hamlet", 2);

        grafoObras.imprimirGrafo(); // Imprime o grafo para visualização

        // Definindo as preferências dos usuários
        Map<String, Set<String>> preferenciasUsuarios = new HashMap<>();
        Set<String> preferenciasUsuario1 = new HashSet<>();
        preferenciasUsuario1.add("Moby Dick");
        preferenciasUsuario1.add("O Grande Gatsby");

        Set<String> preferenciasUsuario2 = new HashSet<>();
        preferenciasUsuario2.add("Cem Anos de Solidão");
        preferenciasUsuario2.add("Dom Quixote");

        preferenciasUsuarios.put("Usuario1", preferenciasUsuario1);
        preferenciasUsuarios.put("Usuario2", preferenciasUsuario2);

        // Executando o algoritmo de recomendação de obras a partir de uma obra inicial
        String obraInicial = "Dom Quixote";
        System.out.println("Iniciando recomendação de obras a partir de: " + obraInicial);
        List<String> obrasRecomendadas = grafoObras.recomendarObras(obraInicial, preferenciasUsuarios);

        // Imprimindo as obras recomendadas
        System.out.println("\nRecomendações de obras:");
        for (String obra : obrasRecomendadas) {
            System.out.println(obra);
        }

    }
}
