# StreamHub - Tema 6 (POO em Java)

Plataforma de streaming em console. Projeto Integrador de POO - Fatec Votorantim (DSM).

## Como rodar (NetBeans)
1. File > Open Project e escolha a pasta do projeto.
2. Botao direito no projeto > Run (a classe principal e `stream.Main`).

## Estrutura (pacotes)
| Pacote | Classes | Papel |
|---|---|---|
| `stream` | `Main` | Menu no console |
| `stream.conteudo` | `Conteudo` (abstrata), `Filme`, `Serie`, `Podcast` | Heranca |
| `stream.usuario` | `SuperClassUsuarios` (abstrata), `Infantil`, `Adulto` | Heranca + controle parental |
| `stream.contrato` | `Avaliavel`, `Remuneravel` | Interfaces |
| `stream.catalogo` | `Catalogo` | Agregacao (ArrayList de Conteudo) |

## Checklist do roteiro
- Classe abstrata com 3 filhas (`extends`): `Conteudo` -> Filme, Serie, Podcast.
- Atributos `private`/`protected` com getters e setters que validam (nao aceitam vazio, negativo ou zero).
- Construtores com parametros.
- `@Override` (sobreposicao): `getDuracaoTotal`, `getTipo`, `calcularRoyalties`, `reproduzir`, `estaValido`, `podeAssistir`.
- Sobrecarga: `Catalogo.buscar(String)`/`buscar(int)`, `listar()`/`listar(usuario)`/`listar(tipo)`, `calcularTotalRoyalties()`/`calcularTotalRoyalties(criador)`.
- Interfaces: `Avaliavel` e `Remuneravel` (implementadas por `Conteudo`).
- Agregacao: `Catalogo` tem um `ArrayList<Conteudo>`.
- `Main`: cria alguns conteudos de exemplo e abre um menu com `Scanner`, `while` e `switch`. Letras no lugar de numeros sao tratadas com `hasNextInt()`.

## Regras do Tema 6
- **Duracao padronizada:** tudo em minutos (`getDuracaoTotal`). Filme: duracao unica. Serie: temporadas x episodios x duracao. Podcast: minutos de audio.
- **Avaliacao:** 1 a 5 estrelas; a media e recalculada a cada nota.
- **Controle parental:** `podeAssistir` no usuario. `Infantil` so assiste faixa <= idade; `Adulto` assiste tudo.
- **Royalties:** pagos ao criador por reproducao real (Filme R$ 0,50, Serie R$ 0,30, Podcast R$ 0,10). Tentativa bloqueada nao conta.
- **Caso de teste:** menu opcao 3: escolha "Noite Sem Fim", nome qualquer e idade 14. Perfil de 14 anos tenta ver "Noite Sem Fim" (18+): aparece ACESSO NEGADO e a tentativa vai para o log de auditoria.

## Polimorfismo (so o que a turma viu)
- **Sobrecarga:** mesmo nome de metodo, parametros diferentes (`listar`, `buscar`).
- **Sobreposicao:** a filha reescreve o metodo da mae com `@Override` (`calcularRoyalties` em cada tipo).

## Mini-glossario do Java usado
- `hasNextInt()`: confere se o proximo valor digitado e inteiro, antes de ler.
- `abstract`: classe que nao pode ser criada com `new`, so herdada.
- `extends`: herda de uma classe. `implements`: assina uma interface.
- `super(...)`: chama o construtor da classe mae. `super.metodo()`: chama o metodo da mae.
- `ArrayList<Conteudo>`: lista que cresce sozinha.

## Perguntas para a defesa
- **Por que `Conteudo` e abstrata?** Nao existe "conteudo generico"; so Filme, Serie ou Podcast.
- **Onde esta a agregacao?** `Catalogo` tem uma lista de `Conteudo`.
- **Como o Infantil e bloqueado?** `Catalogo.reproduzir` chama `conteudo.reproduzir(usuario)`, que pergunta `usuario.podeAssistir(this)`.
- **Qual `calcularRoyalties` roda?** O da classe real do objeto (Filme, Serie ou Podcast).
