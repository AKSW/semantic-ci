'use strict';

let boom = require('boom');

module.exports = {
  assembleResultMessage: function(result) {
    switch (result.stage) {
      case 'syntax':
        return boom.badData('There are some erros in you Dataset.\n' + listSyntaxErrors(result.message));
        break;
      case 'literals':
        return boom.badData('There are some erros in you Dataset.\n' + listLiteralErrors(result.message));
        break;
      case 'vocabs':
        return boom.badData('There are some erros in you Dataset.\n' + listVocabErrors(result.message));
        break;
      default:
        return boom.badData('Seems your Data is wrong but I can\'t process is right. I\'m sorry for not listing any errors.');
    }
  }
};

function listSyntaxErrors(text) {
  let errorArray = text.split('\n')
    .filter((line) => line.startsWith('rapper: Error'))
    .map((line) => line.split(' - '));
  return errorArray.map((arr) => {
    let file = arr[1].split(':');
    return ('Line: ' + file[file.length - 1] + ', ' + arr[2]);
  }).toString();
}

function listLiteralErrors(text) {
  return text;
}

function listVocabErrors(text) {
  return text;
}
