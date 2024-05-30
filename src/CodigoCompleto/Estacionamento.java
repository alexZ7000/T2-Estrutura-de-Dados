package CodigoCompleto;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

abstract class CapacidadeEstacionamento {
    protected final int capacidade;
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public CapacidadeEstacionamento(final int capacidade) {
        this.capacidade = capacidade;
    }

    public abstract void entrarEstacionamento(final String placa);
    public abstract void sairEstacionamento(final String placa);
    public abstract void consultarCarro(final String placa);
    public abstract void statusEstacionamento();
}

class PilhaEstacionamento extends CapacidadeEstacionamento {
    private final Stack<Carro> pilhaEstacionamento;

    public PilhaEstacionamento(final int capacidade) {
        super(capacidade);
        this.pilhaEstacionamento = new Stack<>();
    }

    @Override
    public void entrarEstacionamento(final String placa) {
        if (pilhaEstacionamento.size() >= capacidade) {
            System.out.println("Estacionamento cheio!");
            return;
        }
        final Carro carro = new Carro(placa, LocalDateTime.now());
        pilhaEstacionamento.push(carro);
        System.out.println("Carro " + placa + " entrou no estacionamento às " + carro.tempoDeEntrada().format(formatter));
    }

    @Override
    public void sairEstacionamento(final String placa) {
        final Stack<Carro> tempPilha = new Stack<>();
        boolean encontrado = false;
        int manobras = 0;

        while (!pilhaEstacionamento.isEmpty()) {
            final Carro carro = pilhaEstacionamento.pop();
            if (carro.placa().equals(placa)) {
                encontrado = true;
                final LocalDateTime tempoDeSaida = LocalDateTime.now();
                final Duration duracao = Duration.between(carro.tempoDeEntrada(), tempoDeSaida);
                System.out.println("Carro " + placa + " saiu do estacionamento às " + tempoDeSaida.format(formatter));
                System.out.println("Tempo total de permanência: " + duracao.toMinutes() + " minutos");
                System.out.println("Número de manobras: " + manobras);
                break;
            } else {
                tempPilha.push(carro);
                manobras++;
            }
        }

        while (!tempPilha.isEmpty()) {
            pilhaEstacionamento.push(pilhaEstacionamento.pop());
        }

        if (!encontrado) {
            System.out.println("Carro com placa " + placa + " não encontrado no estacionamento.");
        }
    }

    @Override
    public void consultarCarro(final String placa) {
        int posicao = pilhaEstacionamento.size();
        for (final Carro carro : pilhaEstacionamento) {
            if (carro.placa().equals(placa)) {
                System.out.println("Carro " + placa + " está na posição " + posicao + " da pilha, entrou às " + carro.tempoDeEntrada().format(formatter));
                return;
            }
            posicao--;
        }
        System.out.println("Carro com placa " + placa + " não está no estacionamento.");
    }

    @Override
    public void statusEstacionamento() {
        if (pilhaEstacionamento.isEmpty()) {
            System.out.println("O estacionamento está vazio.");
        } else {
            System.out.println("Carros atualmente no estacionamento:");
            for (final Carro carro : pilhaEstacionamento) {
                System.out.println(carro);
            }
        }
    }
}

class FilaEstacionamento extends CapacidadeEstacionamento {
    private final Queue<Carro> filaEstacionamento;

    public FilaEstacionamento(final int capacidade) {
        super(capacidade);
        this.filaEstacionamento = new LinkedList<>();
    }

    @Override
    public void entrarEstacionamento(final String placa) {
        if (filaEstacionamento.size() >= capacidade) {
            System.out.println("Estacionamento cheio!");
            return;
        }
        final Carro carro = new Carro(placa, LocalDateTime.now());
        filaEstacionamento.add(carro);
        System.out.println("Carro " + placa + " entrou no estacionamento às " + carro.tempoDeEntrada().format(formatter));
    }

    @Override
    public void sairEstacionamento(final String placa) {
        boolean encontrado = false;
        int manobras = 0;
        for (Iterator<Carro> iterator = filaEstacionamento.iterator(); iterator.hasNext(); ) {
            Carro carro = iterator.next();
            if (carro.placa().equals(placa)) {
                encontrado = true;
                iterator.remove();
                LocalDateTime tempoDeSaida = LocalDateTime.now();
                Duration duration = Duration.between(carro.tempoDeEntrada(), tempoDeSaida);
                System.out.println("Carro " + carro.placa() + " saiu do estacionamento às " + tempoDeSaida.format(formatter));
                System.out.println("Tempo total de permanência: " + duration.toMinutes() + " minutos");
                System.out.println("Número de manobras realizadas: " + manobras);
                break;
            } else {
                manobras++;
            }
        }
        if (!encontrado) {
            System.out.println("Carro com placa " + placa + " não encontrado no estacionamento.");
        }
    }


    @Override
    public void consultarCarro(final String placa) {
        int posicao = 1;
        for (final Carro carro : filaEstacionamento) {
            if (carro.placa().equals(placa)) {
                System.out.println("Carro " + placa + " está na posição " + posicao + " da fila, entrou às " + carro.tempoDeEntrada().format(formatter));
                return;
            }
            posicao++;
        }
        System.out.println("Carro com placa " + placa + " não está no estacionamento.");
    }

    @Override
    public void statusEstacionamento() {
        if (filaEstacionamento.isEmpty()) {
            System.out.println("O estacionamento está vazio.");
        } else {
            System.out.println("Carros atualmente no estacionamento:");
            for (final Carro carro : filaEstacionamento) {
                System.out.println(carro);
            }
        }
    }
}

record Carro(String placa, LocalDateTime tempoDeEntrada) {
    @Override
    public String toString() {
        return "\nCarro: placa='" + placa + "', tempo de entrada=" + tempoDeEntrada.format(CapacidadeEstacionamento.formatter);
    }
}

public class Estacionamento {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("\nTrabalho T2 de Estrutura de Dados, feito em JAVA 22!!");
        System.out.println("""
                Integrantes do grupo:\

                Alessandro Lima -> 23.01172-6\

                Marcelo Zoletti -> 23.00171-2\

                Luca Pinheiro -> 23.00335-9\

                Gabriel Merola -> 23.00825-3
                """);

        System.out.println("Selecione o tipo de estacionamento:");
        System.out.println("1. Estacionamento com Entrada Única (Pilha)");
        System.out.println("2. Estacionamento com Entrada e Saída Separadas (Fila)");
        final int escolha = scanner.nextInt();

        final CapacidadeEstacionamento capacidadeEstacionamento;

        if (escolha == 1) {
            capacidadeEstacionamento = new PilhaEstacionamento(5);
        } else if (escolha == 2) {
            capacidadeEstacionamento = new FilaEstacionamento(5);
        } else {
            System.out.println("Opção inválida.");
            return;
        }

        while (true) {
            System.out.println("\nEscolha uma ação:");
            System.out.println("1. Entrada de Carro");
            System.out.println("2. Saída de Carro");
            System.out.println("3. Consulta de Carro");
            System.out.println("4. Status do Estacionamento");
            System.out.println("5. Sair");
            final int leitor = scanner.nextInt();
            scanner.nextLine();

            switch (leitor) {
                case 1:
                    System.out.println("Digite a placa do carro:");
                    final String placaDaEntrada = scanner.nextLine();
                    capacidadeEstacionamento.entrarEstacionamento(placaDaEntrada);
                    break;
                case 2:
                    System.out.println("Digite a placa do carro para saída:");
                    final String placaDaSaida = scanner.nextLine();
                    capacidadeEstacionamento.sairEstacionamento(placaDaSaida);
                    break;
                case 3:
                    System.out.println("Digite a placa do carro para consulta:");
                    final String placaDaConsulta = scanner.nextLine();
                    capacidadeEstacionamento.consultarCarro(placaDaConsulta);
                    break;
                case 4:
                    capacidadeEstacionamento.statusEstacionamento();
                    break;
                case 5:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
