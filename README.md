# AI Game Competition

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1510c3254ba44977919916f780a0861a)](https://app.codacy.com/app/devos-jordi/ai-game-competition?utm_source=github.com&utm_medium=referral&utm_content=scala-academy/ai-game-competition&utm_campaign=badger)
[![Join the chat at https://gitter.im/ai-game-competition/Lobby](https://badges.gitter.im/ai-game-competition/Lobby.svg)](https://gitter.im/ai-game-competition/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Waffle.io - Task board](https://badge.waffle.io/scala-academy/ai-game-competition.svg?columns=all)](https://waffle.io/scala-academy/ai-game-competition)
[![Build Status](https://travis-ci.org/scala-academy/ai-game-competition.svg?branch=master)](https://travis-ci.org/scala-academy/ai-game-competition)

Learning project for training Scala, Akka and Finch skills

Basic idea is to together develop a game server that hosts games.

Learning goals:
 * (Finch) Use finch to create a rest api
 * (Scala) Write logic for some games in scala
 * (Akka) Implement a scalable system that can run and manage multiple games concurrently
 * (CQRS / Sharding / Persistence) implement event sourcing for the gamestate (incl refactoring of gamestate to suit event sourcing)
 * (Gatling) We need a performance test to finetune the implemented features
