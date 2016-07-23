package com.hao.spider.http;

import feign.*;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by donghao on 16/7/23.
 */
public class TestFeign {

    interface GitHub {
        @RequestLine("GET /repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);
    }

    static class Contributor {
        String login;
        int contributions;

    }

    static class GithubClientError extends RuntimeException {
        private String message;

        @Override
        public String getMessage() {
            return message;
        }
    }

    static class GithubErrorDecoder implements ErrorDecoder {
        final Decoder decoder;
        final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

        public GithubErrorDecoder(Decoder decoder) {
            this.decoder = decoder;
        }

        @Override
        public Exception decode(String methodKey, Response response) {
            try {
               return (Exception) decoder.decode(response,GithubErrorDecoder.class);
            } catch (IOException e) {
                return defaultDecoder.decode(methodKey,response);
            }
        }
    }




    @Test
    public void testFeign() {

        GitHub github = HttpClient.getInstance().action(GitHub.class, "https://api.github.com");
        System.out.println("Let's fetch and print a list of the contributors to this library.");
        List<Contributor> contributors = github.contributors("netflix", "eureka");  // 7
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }

        System.out.println("Now, let's cause an error.");
        try {
            github.contributors("netflix", "some-unknown-project");  // 8
        } catch (GithubClientError e) {
            System.out.println(e.getMessage());
        }
    }

}
