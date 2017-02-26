# Community Agents Simulation & Trust/Reputation Research
![AU_Logo](https://github.com/Aarhus-BSS/Aarhus-Research/blob/master/misc/au_standard_logo.jpg) 
![AUBSS_Logo](https://github.com/Aarhus-BSS/Aarhus-Research/blob/master/misc/BSSlogo_blue_DK_01.png)

[![License](https://img.shields.io/hexpm/l/plug.svg)]()
[![ProjectVersion](https://img.shields.io/badge/version-alpha-orange.svg)]()
[![Shippable](https://img.shields.io/shippable/5444c5ecb904a4b21567b0ff.svg)]()

***
This is an early stage development project, many things may change or be removed, including this README.
***

## Installation

For git SSH cloning you need a RSA Key.
```sh
$ git clone git@github.com:Aarhus-BSS/Aarhus-Research.git
```

## Usage

You need to use the GUI, easily understandable, to perform the tests.

If you use Java, include all the source files and use RoundManager as starting point.

```java
RoundManager _roundManager = new RoundManager(_numberOfRounds, _numberOfSAgents, _numberOfPAgents);
_roundManager.runRound(); // Single round run.
_roundManager.runLoop(); // All the rounds.
_roundManager.end(); // Write the end of the report files and ends the Matchmaking game.
```

## Developer PGP

https://pgp.mit.edu/pks/lookup?op=get&search=0x222A698411E7D47D

240068@via.dk / d3v1l401@d3vsite.org

## License

MIT © [Aarhus Universitet](https://au.dk/)