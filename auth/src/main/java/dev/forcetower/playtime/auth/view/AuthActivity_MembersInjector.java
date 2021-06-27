package dev.forcetower.playtime.auth.view;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dev.forcetower.playtime.auth.core.AuthToken;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class AuthActivity_MembersInjector implements MembersInjector<AuthActivity> {
  private final Provider<AuthToken> tokenProvider;

  public AuthActivity_MembersInjector(Provider<AuthToken> tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  public static MembersInjector<AuthActivity> create(Provider<AuthToken> tokenProvider) {
    return new AuthActivity_MembersInjector(tokenProvider);
  }

  @Override
  public void injectMembers(AuthActivity instance) {
    injectToken(instance, tokenProvider.get());
  }

  @InjectedFieldSignature("dev.forcetower.playtime.auth.view.AuthActivity.token")
  public static void injectToken(AuthActivity instance, AuthToken token) {
    instance.token = token;
  }
}
