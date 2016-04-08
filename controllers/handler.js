'use strict';

const boom = require('boom'),
  co = require('../common');

module.exports = {
  validate: function(request, reply) {
    reply(boom.badImplementation());
  }
};
