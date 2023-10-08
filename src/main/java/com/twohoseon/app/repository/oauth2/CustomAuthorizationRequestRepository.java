package com.twohoseon.app.repository.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : CustomAuthorizationRequestRepository
 * @date : 2023/10/07 1:49 PM
 * @modifyed : $
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME = CustomAuthorizationRequestRepository.class.getName() + ".AUTHORIZATION_REQUEST";

    private final String sessionAttributeName = DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {

        Assert.notNull(request, "request cannot be null");
        String stateParameter = getStateParameter(request);
        if (stateParameter == null) {
            return null;
        }
        OAuth2AuthorizationRequest authorizationRequest = getAuthorizationRequest(request);
        return (authorizationRequest != null && stateParameter.equals(authorizationRequest.getState()))
                ? authorizationRequest : null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
                                         HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }
        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");
        request.getSession().setAttribute(this.sessionAttributeName, authorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                 HttpServletResponse response) {
        log.debug("request = " + request);
        Assert.notNull(response, "response cannot be null");
        OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
        if (authorizationRequest != null) {
            request.getSession().removeAttribute(this.sessionAttributeName);
        }
        return authorizationRequest;
    }

    /**
     * Gets the state parameter from the {@link HttpServletRequest}
     *
     * @param request the request to use
     * @return the state parameter or null if not found
     */
    private String getStateParameter(HttpServletRequest request) {
        return request.getParameter(OAuth2ParameterNames.STATE);
    }

    private OAuth2AuthorizationRequest getAuthorizationRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = null;
        if (session == null) {
            request.getSession();
            try {
                oAuth2AuthorizationRequest = (OAuth2AuthorizationRequest) session.getAttribute(this.sessionAttributeName);
            } catch (NullPointerException exception) {
                log.debug("exception = " + exception);
            }
        }
        //oAuth2AuthorizationRequest가 null이면 새로운 oAuth2AuthorizationRequest를 생성해준다.
        if (oAuth2AuthorizationRequest == null) {
            log.debug("request = " + request.getRequestURI());
            return createOAuth2AuthorizationRequest(request);
        }

        return (OAuth2AuthorizationRequest) session.getAttribute(this.sessionAttributeName);

    }

    //세션에서 OAuth2AuthorizationRequest를 얻지 못하면 새로운 OAuth2AuthorizationRequest를 생성해주는 로직
    private OAuth2AuthorizationRequest createOAuth2AuthorizationRequest(HttpServletRequest request) {

        String registrationId = extractRegistrationId(request);
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration == null)
            throw new IllegalArgumentException("Cannot find ClientRegistration with id " + registrationId);

        return OAuth2AuthorizationRequest.authorizationCode()
                .authorizationRequestUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .clientId(clientRegistration.getClientId())
                .redirectUri(clientRegistration.getRedirectUri())
                .scopes(clientRegistration.getScopes())
                .state(request.getParameter("state"))
                .attributes(attributes -> attributes.put("registration_id", registrationId))
                .build();
    }

    private String extractRegistrationId(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] segments = uri.split("/");
        // code/naver, apple, etc.. 에서  registerationId 추출
        return segments[segments.length - 1];
    }
}