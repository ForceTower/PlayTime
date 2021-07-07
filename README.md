# PlayTime
Um app simples para notificar quando um filme foi lançado em DVD ou formato digital

## Executar o Projeto
* Para executar o projeto, será necessário instalar o [Android Studio 4.2.1 ou superior](https://developer.android.com/studio?authuser=1&hl=en-us)
* Uma coisa muito importante sobre esse repositório é que ele possui submodules! Então você precisa clonar os submodules para que tudo funcione!
* Use o comando: `git clone --recurse-submodules -j8 https://github.com/ForceTower/PlayTime.git` para clonar o repositorio e todos os seus submodules de uma só vez.
* Se você fez um git clone sem submodules, execute o comando `git submodule update --init` dentro da pasta do projeto clonado :)

## Submodules? Por que?
O submodule `android-toolkit` é uma coleção de códigos úteis que eu acumulei ao longo do tempo.
Por isso, eles tem seu proprio repositório que recebe atualizações constantes, isso faz com que eu possa evitar reescrita ou "copiar e colar" código entre projetos.
Contudo, como estou adicionando bastante código "desnecessário" em alguns projetos, o uso do Proguard para minimificar, obfuscar e excluir codigo não utilizado se torna quase obrigatório.
