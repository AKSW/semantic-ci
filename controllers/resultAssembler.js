'use strict';

let boom = require('boom');

module.exports = {
  assembleResultMessage: function(result) {
    //TODO geht nicht hier rein
    switch (result.stage) {
      case 'syntax':
        return boom.badData('There are some erros in you Dataset.', listSyntaxErrors(result.message));
        break;
      case 'literals':
        return boom.badData('There are some erros in you Dataset.', listLiteralErrors(result.message));
        break;
      case 'vocabs':
        return boom.badData('There are some erros in you Dataset.', listVocabErrors(result.message));
        break;
      default:
        return boom.badData('Seems your Data is wrong but I can\'t process is right. I\'m sorry for not listing any errors.');
    }
  }
};

function listSyntaxErrors(text) {
  let errs = text.split('\n').filter((line) => line.startsWith('rapper: Error')).map((line) => line.split(' - '));
  return errs.map((arr) => {
    let file = arr[1].split(':');
    return 'Syntax error at line: ' + file[file.length - 1] + '\n' + arr[2];
  }).toString();
}

function listLiteralErrors(text) {

}

function listVocabErrors(text) {

}
