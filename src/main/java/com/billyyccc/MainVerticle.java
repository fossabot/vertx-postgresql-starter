/*
 * MIT License
 *
 * Copyright (c) 2017 Billy Yuan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.billyyccc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * MainVerticle class to deploy the verticles needed.
 *
 * @author Billy Yuan <billy112487983@gmail.com>
 */

public class MainVerticle extends AbstractVerticle {
  private static final String HTTP_SERVER_VERTICLE_IDENTIFIER = "com.billyyccc.http.HttpServerVerticle";
  private static final String PG_DATABASE_VERTICLE_IDENTIFIER = "com.billyyccc.database.BookDatabaseVerticle";

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    Future<String> dbVerticleDeployment = Future.future();
    vertx.deployVerticle(PG_DATABASE_VERTICLE_IDENTIFIER, dbVerticleDeployment.completer());

    dbVerticleDeployment.compose(id -> {
      Future<String> httpVerticleDeployment = Future.future();
      vertx.deployVerticle(HTTP_SERVER_VERTICLE_IDENTIFIER,
        httpVerticleDeployment.completer());
      return httpVerticleDeployment;
    }).setHandler(ar -> {
      if (ar.succeeded()) {
        startFuture.complete();
      } else {
        startFuture.fail(ar.cause());
      }
    });
  }
}
