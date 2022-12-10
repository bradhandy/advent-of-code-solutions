parser grammar NineteenParser;

options {
    language = Java;
    tokenVocab = 'com/dbjgb/advent/_2020/puzzle/_19/NineteenLexer';
}

@header {
package com.dbjgb.advent._2020.puzzle._19;
}

valid_command: A0 EOF;