# 📱 Trabalho Final - Programação Para Dispositivos Móveis - P02-2024-1
## Sumário
- [📍 Introdução](#introdução)
- [❗ Informações iniciais ](README.md/#informações-iniciais)
- [🗺️ Albumap - Um grande mapa de memórias ](#albumap---um-grande-mapa-de-memórias)
- [🛠️ Implementação do trabalho - O que será entregue neste repositório](#implementação-do-trabalho---o-que-será-entregue-neste-repositório)
    - [Papéis](#papéis)
    - [Requisitos funcionais](#requisitos-funcionais)
    - [Requisitos não funcionais](#requisitos-não-funcionais)
    - [Requisitos de interface](#requisitos-de-interface)
    - [Justificativas](#justificativas)

## Introdução
Este repositório se dedica à implementação de um aplicativo que atenda suficientemente os requisitos publicados para a conclusão do "Trabalho final de ProgMov 2024", da disciplina de **Programação Para Dispositivos Móveis** ofertada ao curso de **Engenharia de Software**, da Faculdade de Computação (**FACOM**), na Universidade Federal do Mato Grosso do Sul (**UFMS**), ministrada pela Profª Ana Karina. O aplicativo será baseado em uma ideia autoral, o aplicativo "Albumap", explicado ao longo deste arquivo.

## Informações iniciais
**Aluno:**

Nome: Lohan Toledo Tosta

RGA: 2021.1906.006-9

## Albumap - Um grande mapa de memórias

### Premissa
>Tirar e publicar fotos já não é novidade para ninguém! Com o advento dos smartphones e das câmeras fotográficas, tirar fotos, gravar vídeos e editar tudo isso vem se tornando cada dia mais intuitivo e parte da vida de todos nós. E isso já é muito bem sabido e explorado por diversos tipos de aplicativos. São inúmeros os aplicativos voltados não somente à criação desse tipo de mídia, mas principalmente a publicação e exposição. Mas será que exploramos a publicação de forma ampla? Será que realmente é satisfatório a forma como nós mostramos ao mundo e a nós mesmos as nossas próprias criações?

>Com a evolução das redes sociais em torno da famosa _timeline_, nos acostumamos a consumir conteúdo organizado esssencialmente em torno da cronologia. Nossos perfis exibem sempre as publicações mais recentes e nossos _feeds_ exibem (com alguma adaptação) aquilo que é mais recente. É claro que esse tipo de exibição possui o seu valor e motivo para ser o mais bem difundido. Mas há como ir além: com **Albumap**, teremos uma nova forma de visualizar nosas queridas memórias.

>**Albumap** é um _hub_ voltado à oferecer uma nova forma de visualizar as fotos que tiramos e publicamos. Por meio de um mapa digital, as fotos são expostas de acordo com a localização delas, permitindo que o usuário disfrute de suas memórias sempre as relacionando com o local onde foram feitas. O foco da exposição é o espaço e não mais o tempo, embora ele ainda seja parte do processo exploratório.

### Features principais
- Filtro de exibição por tags - As mídias podem ser categorizadas por tags e, por meio delas, o usuário pode refinar a visualização
- Inclusão/Remoção de mídia - O usuário pode incluir novas mídias ao aplicativo usando a câmera ou então importando da galeria nativa
- Alteração de localização - O usuário pode alterar a localização das mídias a qualquer momento.
- Modos de visibiliade - O usuário pode usar o aplicativo de forma privada e selecionar as mídias que estarão públicas. Quando públicas, o usuário também pode selecionar a visibilidade de cada tag
- Gerenciamento de comunidades - O usuário pode criar comunidades e gerenciar seus participantes e permissões. As comunidades podem ter tags próprias e é possível que elas sejam alimentadas com mídias de forma colaborativa
- Social Hub - Espaço por onde o usuário pode visualizar as diversas tags, offline, privadas e públicas. Também podendo filtrá-las.
- Visualização de perfis - O usuário pode buscar por perfis de outros usuários ou comunidades e visualizar as mídias das tags públicas (ou privadas que ele possua acesso) destes usuários

## Implementação do trabalho - O que será entregue neste repositório
Para a entrega deste trabalho, o foco é a implementação de adaptações de algumas _features_ do aplicativo. Abaixo, seguem as features mínimas e justificativas de acordo com o documento de requisitos do trabalho final
### Papéis
- **Usuário comum:** Para utilizar o aplicativo ele deve estar logado. A autenticação será feita com base no banco de dados local. - [J1/J2](#justificativas)

### Requisitos funcionais
- **Cadastro de usuários:** A aplicação deve permitir o cadastro de usuários com os campos: email, nome de usuário, senha e foto de perfil - [J2/J3](#justificativas)
- **Login:**: A aplicação deve permitir o acesso as suas funcionalidades apenas por meio de login com email/nome de usuário e senha - [J1](#justificativas)
- **Visualização do mapa digital:** A aplicação deve permitir visualizar em tela inicial, o mapa digital. O mapa deve mostrar as fotos cadastradas - [J12](#justificativas)
- **Filtros no mapa digital:** A aplicação deve permitir aplicar filtros no mapa digital, selecionando as fotoso por: tag, período
- **Inclusão de nova mídia na aplicação:** A aplicação deve permitir, a partir dos arquivos locais ou diretamente da câmera, incluir nova mídia na aplicação, informando, se necessário, a localização da mídia e, se desejado, a(s) tags correspondentes [J7/J11](#justificativas) 
- **Gerenciamento de tags:** A aplicação deve permitir a criação, alteração e deleção de tags com os campos: descrição, cor
- **Gerenciamento de localizações:** A aplicação deve permitir a inclusão (para mídia sem localização) ou alteração de localizações das mídias dentro da aplicação
- **Gerenciamento de datas:** A aplicação deve permitir a inclusão (para mídia sem data) ou alteração de data das mídias dentro da aplicação
- **Datas comemorativas:** O usuário deve ser notificado em datas comemorativas, baseando-se na data das suas mídias[J9](#justificativas)
### Requisitos não funcionais
- **Strings:** As strings da aplicação devem estar contidas no arquivo strings.xml - [J5](#justificativas)
- **Cores:** As cores da aplicação devem estar contidas no arquivo cores.xml - [J6](#justificativas)
- **Cripografia em Hash:** A senha deve ser salva no banco em string criptografada - [J13](#justificativas)
### Requisitos de interface
- **Fragmentos:** A aplicação deve ter uma navegação em abas entre os fragmentos de feed (mapa geral) e perfil do usuário - [J4](#justificativas)
- **Sons:** A aplicação deve emitir sons conforme a interação do usuário - [J8](#justificativas)
- **Menu:** A aplicação deve ter um menu de acesso à outras telas do sistema, como edição dos dados do usuário, ou configuração de notificações, ou configuração do som [J10](#justificativas)

## Justificativas
**J1** [...] a) fazer testes de caixa preta e tratar erros como: -> um usuário não cadastrado conseguir acessar o sistema [...]

**J2** [...] b) Ter uma tela inicial de login com cadastro de usuários.[...]

**J3** [...] [...] ->Opcionalmente armazenar a foto do usuário.[...]

**J4:** [...]fragmentos[...]

**J5:** [...]strings[...]

**J6:** [...]cores[...]

**J7:** [...]imagens[...]

**J8:** [...]sons[...]

**J9:** [...]notificações[...]

**J10:** [...]menu[...]

**J11:** [...]câmera[...]

**J12:** [...]mapas[...]

**J13:** [...]->Segurança (Como restringir o acesso dos usuários aos recursos necessários, criptografia de informações importantes como o hash da senha, etc)[...]





