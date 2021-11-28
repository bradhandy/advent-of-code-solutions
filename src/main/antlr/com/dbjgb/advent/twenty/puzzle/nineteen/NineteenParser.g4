parser grammar NineteenParser;

options {
    language = Java;
    tokenVocab = 'com/dbjgb/advent/twenty/twenty/puzzle/nineteen/NineteenLexer';
}

@header {
package com.dbjgb.advent.twenty.puzzle.nineteen;
}

valid_command: A0 EOF;