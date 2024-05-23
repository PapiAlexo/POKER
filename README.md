# Póker
##### _Integrantes: Alejandro Ortiz, Daniel Martín Bartolomé, Ignacio Pezuela_
### 1- ¿Por qué elegimos este proyecto?
Nuestra objetivo era crear un juego en el que el usuario se enfrentase a una máquina que nosotros mismos codificaríamos. El primer juego en el que pensamos fue el ajedrez, pero hacerlo bien requeriría crear una buena interfaz gráfica, 
 por lo que nos decantamos por el Póker. Este juego era más original, llamativo y además seguiríamos programando una máquina que "pensase por sí misma".
### 2- Explicación del programa
Basado en la famosa versión de Póker "Texas Hold'em", este programa brinda la oportunidad de probarlo sin llegar a gastar dinero. Cada jugador comienza con 2 cartas, mientras que en la mesa se van revelando a medida que avanza la ronda. 
 El juego se divide en 4 fases: **PRE-FLOP**, **FLOP**, **TURN** y **RIVER**. 
* En la **PRE-FLOP** no hay cartas en la mesa, por eso también se conoce a esta fase como **LA CIEGA**.
* En la **FLOP** se reparten 3 cartas a la mesa, dando la oportunidad a los jugadores de pensar si continuar o abandonar.
* En la **TURN** se reparte una carta más a la mesa.
* Finalmente, en la **RIVER** se reparte la última carta a la mesa y se brinda la última oportunidad de abandonar. Una vez pasada esta ronda, se decide el ganador y se le obsequia con el dinero que se ha ido apostando en las fases.

  |  Mano  |  Contenido  |
  |--------|-------------|
  |Escalera de color| 5 cartas consecutivas del mismo palo|
  |Poker| 4 cartas de igual valor|
  |Full| 3 cratas y 2 cartas de igual valor|
  |Color| 5 cartas del mismo palo|
  |Escalera| 5 cartas consecutivas|
  |Trio| 3 cartas de igual valor|
  |Doble Pareja | 2 pares de dos cartas de mismo valor|
  |Pareja| 2 cartas de mismo valor|
  |Carta alta| La carta mas alta de tu mano|
  
  
### 3- Problemas que hemos afrontado
La creación de la máquina no fue sencilla, la idea de que tendría que eleguir que hacer en cada ronda nos llevó a un rompecabezas de cómo valorar su mano en cada ronda.
 La solución que se nos ocurrió fue crear una clase igual a **Mano** pero exigiendo menos a las manos, por ejemplo, en vez de buscar 5 cartas para un color buscamos 3.

También tuvimos problemas codificando el **Registrador**. Una clase que almacena en dos archivos diferentes las estadísticas de victorias del jugador y la máquina. En especial, a la hora de registrar el porcentaje de victorias, 
tuvimos que hacer diferentes métodos según el orden y quién ganase, pues si, por ejemplo, ganaba la máquina primero, teniamos que actualizar el archivo del jugador teniendo en cuenta la última victoria de la máquina.

Por último, tuvimos problemas en el **Main** con la mecánica de hacer *ALL-IN*. Tuvimos que implementar varios métodos y booleans para que a la hora de que el jugador o la máquina hiciese *ALL-IN*, 
el programa se dirigiese directamente al final, para poco después decidir el ganador comparando sus manos con las cartas en la mesa y registrar la victoria.
### 4- Investigación
Nos tuvimos que documentar respecto a las diferentes combinaciones que hay en el Póker y las fases que hay en este estilo de juego.

Además, investigamos desde 0 la lectura y escritura de archivos para crear el **Registrador**, pero nos bastó con buscar algunos vídeos en Internet para entenderlo.
### 5- Código destacable
La **Máquina** y la **Mano** son definitivamente la guinda del pastel. Conllevó una buena planificación del código de antemano y unas cuantas horas de trabajo, pero al final nos llevó a tener una experiencia más inmersiva y realista acorde a las 
inteligencias artificales que existen hoy en día.
