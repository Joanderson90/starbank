## MI Concorrência e Conectividade - Problema 2 Transações Bancárias Distribuídas.
Este relatório tem o objetivo de descrever os detalhes do desenvolvimento de um sistema baseado na Arquitetura em Rede, utilizando a linguagem de programação Java.

## Conteúdos 

&nbsp;&nbsp;&nbsp;[**1.** Introdução](#introducao)

&nbsp;&nbsp;&nbsp;[**2.** Fundamentação Teórica](#fundamentacao_teorica)

&nbsp;&nbsp;&nbsp;[**3.** Metodologia](#metodologia)

&nbsp;&nbsp;&nbsp;[**4.** Resultados](#resultados)

# <a id="introducao"></a>
## Introdução
Um banco central é deveras importante para os bancos comerciais que atuam em um determinado país, entretanto alguns países não possuem esta entidade central para implementação de uma política monetária, direcionamento da comunicação e transações entre os outros tipos de bancos.

Nesse sentido, foi solicitado aos alunos do curso de Engenharia de Computação da Universidade Estadual de Feira de Santana(UEFS), que desenvolvessem um sistema distribuído para permitir a comunicação e a troca de dados entre um conjunto de aplicações que juntas formam um consórcio bancário, sem a presença de uma entidade central para direcionar as transações.

Este sistema foi desenvolvido utilizando a linguagem de programação Java em conjunto com o framework Spring Boot, este utilizado para construção da API REST de cada um dos bancos.
O sistema permite que os usuários façam as seguintes operações: criação de contas unitárias, isto é, apenas para si, criação de contas conjuntas, realização de transferências entre contas do mesmo bancos e contas de bancos diferentes, saques, depósitos e visualização do saldo atual de sua conta.

Após o desenvolvimento do sistema em questão, foi utilizado containers docker para virtualização da aplicação e execução dos testes.

# <a id="fundamentacao_teorica"></a>
## Fundamentação Teórica
- Uma API REST (também chamada de API RESTful ou web API RESTful) é uma interface de programação de aplicativos (API) que segue os princípios de design do estilo arquitetônico de transferência de estado representacional (REST). A API Rest foi utilizada no problema para realizar a comunicação entre os bancos.
- O Token Ring é um protocolo de redes criado pela IBM nos anos 80. Ela usa a topologia lógica de anel e funciona na camada física, pela ligação de dados, e de união com o modelo OSI de acordo com a aplicação. As redes Token Ring são diferentes das redes Ethernet, muito utilizadas hoje e com uma topologia lógica de barramento. Com um símbolo formado por uma trama de três bytes (token), o funcionamento circula em uma topologia de anel onde as estações aguardam a recepção para serem transmitidas. A transmissão acontece em uma janela de tempo e pelas que possuem o token. Neste projeto, o token ring foi utilizado para resolver o problema das requisições concorrentes entre os bancos envolvendo uma mesma conta.
- O mecanismo de MUTual EXclusion é usado em ambientes concorrentes e é uma forma de impedir que um recurso seja acessado por mais de uma linha de processamento ao mesmo tempo. Ele é usado principalmente para evitar a condição de corrida, para manter uma operação atômica. A exclusão mútua neste projeto, foi utilizada para resolver o problema de transações concorrentes em um mesmo banco(servidor) envolvendo uma mesma conta.
- O bloco try-catch é uma funcionalidade que permite que o programador lide com exceções que ocorram, controlando o fluxo de execução do código em situações imprevistas. Caso ocorra alguma exceção dentro do bloco try, a execução do código será desviada para dentro do bloco catch correspondente, permitindo um maior controle do fluxo do código sendo executado. Neste projeto, este bloco foi utilizado para capturar um erro caso ocorra em uma transação e, desfazê-la por completo caso isto aconteça.
- Job Scheduler é uma ferramenta de agendamento de tarefas que permite automatizar a execução de processos em um sistema operacional ou aplicativo. Esta ferramenta foi utilizada no projeto para automatizar o processo de verificar se uma determinada transação externa foi finalizada com sucesso.
- Containers são ambientes isolados que podem ser instalados em computadores e serem utilizados para seus devidos fins de forma que não é necessário instalar o serviço dentro do computador como uma aplicação, então caso um container dê problema, como ele é um ambiente isolado ele pode ser deletado e uma outra instância deste container pode ser reinstalado.
- Docker é um software open source que permite a criação de container e armazenamento dos mesmo em repositórios na internet, mas também que faz a virtualização em nível de sistema operacional de suas aplicações em seus contêineres nos computadores facilitando a instalação e remoção sem afetar a máquina física caso seja necessário.

# <a id="metodologia"></a>
## Metodologia

#### Token Ring
Se tratando de um sistema distribuído, onde se faz possível dois ou mais servidores acessarem uma determinada área crítica ao mesmo tempo, que neste caso seria uma determinada transação bancária, como uma transferência, se fez necessário o uso de um algoritmo para  resolver o problema da exclusão mútua. Já que como dito anteriormente, dois servidores poderiam realizar uma transação de saque e outra de depósito envolvendo a mesma conta ao mesmo tempo, o que poderia resultar em um valor invalido na conta bancária do usuário. Portanto, para resolver este problema, foi utilizado o algoritmo token ring, com o objetivo de permitir que apenas o servidor que tivesse o token pudesse realizar as transações quando solicitadas, evitando assim que dois servidores realizassem uma transação em uma mesma conta ao mesmo tempo. Destarte, foi criado uma um rota em cada servidor para receber o token e passá-lo ao próximo servidor.

Neste projeto, cada servidor ficou em posse do token por 3 segundos, ou seja durante 3 segundos o servidor poderia realizar todas transações solicitadas até o momento e, as que fossem solicitadas enquanto este estivesse em posse do token. Caso o servidor recebesse uma requisição enquanto não estivesse de posse do token, a requisição permaneceria em espera para ser executada até o servidor receber o token.


#### Mutex
Além da possibilidade de dois servidores realizarem uma transação ao mesmo tempo envolvendo uma mesma conta bancária, também existiu a possibilidade de dois usuários diferentes solicitarem uma transação para a mesma conta em um mesmo banco. Isso porque, no projeto em questão, houve a possibilidade de contas conjuntas, isto é, dois ou mais usuários compartilharem a mesma conta. Tendo isso em vista, se fez necessário tratar a possibilidade de dois usuários realizarem uma transação de forma concorrente, em uma mesma conta. Dessa forma, foi implementado o algoritmo mutex com o propósito de permitir que apenas um usuário por vez realizasse uma determinada transação para a mesma conta. Para isso, foi implementado uma lista para armazenar o número da conta bancária que estivesse na área crítica. Com isso, quando um determinado usuário solicitasse uma determinada ação em sua conta bancário, era verificado na lista citada anteriormente, se o número da conta presente na requisição estava ou não presente na área crítica, se sim, o sistema entrava em um looping até a conta sair da área crítica, senão a conta em questão entraria na lista de contas que estavam acessando a área crítica e era permitido realizar a transação, após o término da transação esta conta era removida da lista. Com isso, apenas uma transação por vez era permitido em uma conta, independente de quantas solicitações fossem feitas ao mesmo tempo.

#### Bloco try-catch
Este bloco foi utilizado nas transações para capturar e desfazer estas caso ocorresse alguma erro, por exemplo, se um banco A fizesse uma requisição de saque para o banco B durante uma transação de transferência bancária, caso ocorresse alguma erro no banco B durante o pedido de saque feito pelo banco A, todas as operações bancárias feitas até momento, envolvendo esta transação, seriam desfeitas. Similar ao conceito de operação atômica, onde uma transação é feita completamente, ou abortada completamente caso haja algum problema. Portanto, todo o processo de uma transação, como o de transferência, ficou encapsulada dentro do bloco try e, caso houvesse algum erro, todas as transações feitas até aquele momento eram desfeitas dentro do bloco catch.

#### Job Scheduler
No quesito confiabilidade das transações, se fez necessário o uso de um job, isto é, uma função que foi executada em um intervalo de 10 segundos, com o propósito de verificar se uma determinada transação feita de um servidor externo, como um saque, foi finalizada com sucesso, se sim, a transação não era desfeita, se não a transação era identificada e desfeita no banco em questão, garantindo assim que nenhum saldo a mais ou a menos fosse imposta em uma determinada conta.


# <a id="resultados"></a>
## Resultados
### Troca de informações entre os bancos
Como descrito anteriormente, não houve um banco central para centralizar todas operações e transações entre os bancos, nesse sentido foi feito um consórcio entre os bancos para viabilizar essa troca de informações. Para cada banco foi criada uma API REST para permitir essa troca de informações. Que funcionou como esperado, isto é a troca de informações foi realizada com sucesso entre os bancos. Para tal foi utilizado o framework Spring Boot e a biblioteca WebClient, esta que facilita a comunicação entre APIs em diferentes projetos.


### Transações entre diferentes bancos
Uma das especificações do projeto proposto, era permitir que transações de transferência pudessem envolver mais de uma conta de origem, isto é, conta que seria debitado o saldo. Nesse sentido, um cliente logado em uma conta presente no banco banco A, poderia usar outra conta de sua posse em um banco B como origem de saque para transferir para um banco C. Consequentemente, todos os bancos deveriam se comunicar para permitir esse tipo de transferência. Para tal, tornou-se necessário solicitar ao cliente quais contas ele queria tirar o dinheiro. Portanto, foi usado uma lista no corpo da  requisição, correspondente às contas de origens, além da conta que ele queria realizar a transferência. Com isso, se fez necessário o monitoramento de todos os bancos envolvidos na transação caso houvesse algum erro em um destes para a transação ser desfeita. Este requisito foi cumprido de forma que a cada solicitação de um banco A para um banco B, se fez necessário verificar se a requisição foi finalizada com sucesso para continuar com a transação, se não esta era abordada e todas transações feitas até o momento eram desfeitas, para evitar valores inválidos nas contas que participavam da transação, seguindo o modelo de transação atômica. Ademais, como dito na seção de metodologia, foi utilizado um job para verificar se uma transação feita por um servidor, banco externo foi completada com sucesso, para manter a transação envolvendo as contas em questão. Garantindo assim, confiabilidade das transações envolvendo mais de um servidor ao mesmo tempo.

### Servidor do Banco
Como dito nas seções anteriores, o servidor do banco foi construído utilizando  o framework Spring Boot, que auxilia no processo de criação de API Web. Foi criada uma api que pode ser utilizada para simular vários bancos. Dessa forma, se faz necessário passar como variável de ambiente apenas os endereços de cada banco envolvido na rede anel. 
O servidor pode fazer o controle de criações de contas bancárias, onde se faz necessário passar apenas a lista com os dados de cada usuário pertencente a conta, e a senha, além de permitir fazer as transações bancárias mais comumentes realizadas, como transferência de banco para banco ou para outros bancos, depósito e verificação de saldo.

### Docker
Cada aplicação acima possui uma imagem docker que foi disponibilizada no repositório online do docker hub. Para executar uma das aplicações é necessário abrir o terminal de sua preferência e ir para a pasta raiz da aplicação desejada e executar o seguinte comando:

```
$ docker compose up
```
