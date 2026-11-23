# StreamHub — Plataforma de Streaming de Mídias

Projeto Integrador de POO (Técnica de Programação I) — FATEC Votorantim, DSM. **Tema 6.**

Sistema de console em Java para gerenciar um catálogo de **filmes, séries e episódios de podcast**, com controle parental por faixa etária (contas Infantil e Adulto), avaliação por estrelas e cálculo de royalties pagos aos criadores.

## Como abrir e rodar

No NetBeans: `File > Open Project` e selecione a pasta do projeto (projeto Ant). A classe principal já está configurada como `stream.Main`. O código usa apenas recursos do Java 17, então roda em qualquer JDK 17 ou superior.

> Os textos do console estão sem acento de propósito, para não aparecerem símbolos quebrados no Output do NetBeans no Windows.

## Estrutura de pacotes

```
src/stream/
├── Main.java                  menu com Scanner / while / switch
├── conteudo/                  Conteudo (abstrata), Filme, Serie, Podcast
├── usuario/                   SuperClassUsuarios (abstrata), Infantil, Adulto
├── contrato/                  Avaliavel, Remuneravel (interfaces)
└── catalogo/                  Catalogo (agregação + sobrecarga + log de auditoria)
```

## Checklist do roteiro → onde está no código

| Requisito | Onde está |
|---|---|
| Atributos `private`/`protected`, getters/setters com validação, construtores parametrizados | Todas as classes. Os setters rejeitam nulo, vazio, negativo e zero (ex.: `Conteudo.setNome`, `Filme.setDuracao`, `SuperClassUsuarios.setIdade`) |
| Superclasse abstrata + 2 ou mais subclasses com `extends` | `Conteudo` → `Filme`, `Serie`, `Podcast`; e também `SuperClassUsuarios` → `Infantil`, `Adulto` |
| `@Override` | `getDuracaoTotal`, `getTipo`, `calcularRoyalties`, `reproduzir`, `exibirDetalhes` nas subclasses de `Conteudo`; `podeAssistir`, `getTipoConta`, `setIdade` nas contas |
| Sobrecarga | `Catalogo.buscar(String)` / `buscar(int)`; `listar()` / `listar(SuperClassUsuarios)` / `listar(String)`; `calcularTotalRoyalties()` / `calcularTotalRoyalties(String)` |
| Interface | `Avaliavel` e `Remuneravel`, implementadas por `Conteudo` |
| Agregação com `ArrayList<Superclasse>` | `Catalogo` guarda `ArrayList<Conteudo>` (lista privada; `getConteudos()` devolve uma cópia) |
| Menu com `Scanner`, `while` e `switch` | `Main` (letra no lugar de número pede de novo, não quebra) |

## Divisão sugerida entre os 5 integrantes

| Integrante | Arquivos |
|---|---|
| 1 — Arquiteto de Domínio | `Conteudo`, `Avaliavel`, `Remuneravel` |
| 2 — Especialização A | `Filme`, `Serie` |
| 3 — Especialização B | `Podcast`, `SuperClassUsuarios`, `Infantil`, `Adulto` |
| 4 — Estruturas e Agregação | `Catalogo` |
| 5 — Interface e Testes | `Main` + roteiro da apresentação |

## Como o sistema resolve os desafios do Tema 6

- **Contagem de tempo padronizada:** todo conteúdo usa `java.time.Duration`, e `getDuracaoTotal()` é abstrato em `Conteudo`. Filme e podcast devolvem a duração única; a série soma a duração de todos os episódios de todas as temporadas.
- **Notas de 1 a 5 estrelas:** `Conteudo.avaliar(int)` rejeita notas fora do intervalo e recalcula a média (`nota`) a cada nova avaliação.
- **Controle parental:** `Conteudo.reproduzir(SuperClassUsuarios)` pergunta ao usuário se ele `podeAssistir` o conteúdo. A conta `Infantil` (menor de 18) só libera o que tem faixa menor ou igual à idade; a `Adulto` libera tudo. Cada subclasse de conteúdo reescreve `reproduzir`, chama `super.reproduzir(...)` e só mostra a sua mensagem se foi liberado. O `Catalogo.reproduzir(...)` registra toda tentativa bloqueada no log de auditoria.
- **Royalties pela reprodução real:** reproduções × valor por tipo (Filme R$ 0,50, Série R$ 0,30, Podcast R$ 0,10). Esses valores são de exemplo, definidos pelo grupo; ajustem se o professor passar outros.

## O que mudou em relação ao que já estava nas branches

- **Projeto NetBeans:** `build.xml`, `nbproject/project.xml`, `nbproject/build-impl.xml`, `nbproject/genfiles.properties` e `nbproject/project.properties` tinham marcadores de conflito de merge (`<<<<<<< Updated upstream`) e o NetBeans não conseguia abrir o projeto. Foi mantido o lado `Stream` / `main.class=stream.Main`.
- **Pacotes:** as classes foram separadas em pacotes. `filme.java` virou `Filme.java` e `serie.java` virou `Serie.java` (o arquivo precisa ter o nome da classe pública). O `usuario.java` vazio foi removido porque a classe `stream.usuario` colidiria com o pacote `stream.usuario`.
- **`Conteudo`:** manteve `nome`, `categoria`, `faixa` e `nota`, e ganhou `criador` (necessário para os royalties) e validações. A `nota` agora é a média calculada por `avaliar()`. A `duracao` saiu da superclasse porque uma série não tem uma duração única.
- **`Serie`:** a lista `temporadas` (`ArrayList<ArrayList<Duration>>`) agora está declarada, com `addTemporada()` e `addEpisodio(temporada, duracao)`.
- **Usuários:** `SuperClassUsuarios` ficou abstrata, com `nome`, `idade`, `podeAssistir` e `getTipoConta`. O `Sexo` e o `Verificacao()` saíram, porque nenhuma regra do Tema 6 usa o sexo e a verificação virou o próprio polimorfismo entre `Infantil` e `Adulto`. O `Infantil` apontava para `SuperClassUsuario` (singular, inexistente) e foi corrigido. O `Adulto` é novo.

## Roteiro da demonstração (opção 9 do menu)

1. Liste o catálogo (opção 2).
2. Rode a opção **9**: um perfil de 14 anos tenta reproduzir *Noite Sem Fim* (18 anos). O sistema bloqueia, mostra o aviso e registra no log. Em seguida um perfil adulto assiste normalmente.
3. Opção **8** mostra o log de auditoria.
4. Opção **6** troca o perfil (menor de 18 vira conta Infantil, 18 ou mais vira Adulto); opção **4** reproduz conteúdos diferentes (cada tipo mostra sua própria mensagem — polimorfismo).
5. Opção **5** avalia (tente a nota 7 para mostrar a validação) e a opção **7** mostra os royalties.
6. Opção **1** cadastra um conteúdo novo (tente faixa 15 ou duração negativa para mostrar as validações).

## Perguntas que podem cair na defesa

- **Por que `Conteudo` é abstrata?** Não existe "um conteúdo genérico": ela obriga cada subclasse a definir a duração total, o tipo e os royalties.
- **Por que a lista é `ArrayList<Conteudo>` e não `ArrayList<Filme>`?** Por causa do polimorfismo: uma lista só guarda qualquer subclasse, e cada objeto executa a sua versão do método.
- **Quem decide se o usuário pode assistir?** A conta. `Infantil.podeAssistir` compara a faixa com a idade; `Adulto.podeAssistir` sempre libera. O `Conteudo` só pergunta, sem saber qual conta é.
- **Por que `getConteudos()` devolve uma cópia?** Para quem chamar não conseguir alterar a lista interna (sem vazamento de escopo).
- **Por que notas e reproduções não têm setter público?** Para ninguém "inventar" uma média ou uma reprodução; só os métodos `avaliar` e `reproduzir` alteram esses valores.
- **Por que os setters são `final`?** Eles são chamados dentro do construtor; sendo `final`, nenhuma subclasse consegue reescrever e burlar a validação. Só `setIdade` não é `final`, de propósito: `Infantil` e `Adulto` reescrevem para garantir que a idade combina com o tipo de conta.
- **Qual a diferença entre sobrecarga e sobrescrita?** Sobrecarga: mesmo nome, parâmetros diferentes (`Catalogo.listar`). Sobrescrita: a subclasse reescreve um método da mãe com `@Override` (`Filme.reproduzir`).
