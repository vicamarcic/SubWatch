package vica.SubWatch.events;

public record UserRegisteredEvent(Long userId, String email, String displayName) {}
