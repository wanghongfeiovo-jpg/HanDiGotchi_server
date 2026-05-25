package com.handigotchi.controller;

import com.handigotchi.game.GameService;
import com.handigotchi.model.CharacterSelectRequest;
import com.handigotchi.model.GameStatusDto;
import com.handigotchi.model.ItemRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/status")
    public GameStatusDto status() {
        return gameService.getStatus();
    }

    @PostMapping("/reset")
    public GameStatusDto reset() {
        return gameService.reset();
    }

    @PostMapping("/restart")
    public GameStatusDto restart() {
        return gameService.restart();
    }

    @PostMapping("/select-character")
    public GameStatusDto selectCharacter(@RequestBody CharacterSelectRequest request) {
        return gameService.selectCharacter(request.getType());
    }

    @PostMapping("/egg/click")
    public GameStatusDto eggClick() {
        return gameService.eggClick();
    }

    @PostMapping("/feed/meal")
    public GameStatusDto feedMeal(@RequestBody ItemRequest request) {
        return gameService.feedMeal(request.getId());
    }

    @PostMapping("/feed/snack")
    public GameStatusDto feedSnack(@RequestBody ItemRequest request) {
        return gameService.feedSnack(request.getId());
    }

    @PostMapping("/play")
    public GameStatusDto play() {
        return gameService.play();
    }

    @PostMapping("/sleep")
    public GameStatusDto sleep() {
        return gameService.toggleSleep();
    }

    @PostMapping("/med")
    public GameStatusDto med() {
        return gameService.cureSick();
    }

    @PostMapping("/clean")
    public GameStatusDto clean() {
        return gameService.cleanPoop();
    }

    @PostMapping("/happy-ending/continue")
    public GameStatusDto happyEndingContinue() {
        return gameService.continueAfterHappyEnding();
    }

    @PostMapping("/happy-ending/restart")
    public GameStatusDto happyEndingRestart() {
        return gameService.restartFromHappyEnding();
    }
}
