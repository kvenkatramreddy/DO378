package org.acme.conference.session;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class SessionStore {

    @ConfigProperty(name = "features.session-integration")
    private boolean sessionIntegration;

    @RestClient
    private SpeakerService speakerService;


    @Inject
    private SessionRepository repository;

    public SessionStore() {
    }

    public Collection<Session> findAll() {
        return repository.findAll().list();
    }

    @Transactional
    public Session save(Session session) {
        repository.persist(session);
        return session;
    }

    @Transactional
    public Optional<Session> updateById(String sessionId, Session session) {
        Optional<Session> sessionOld = findById(sessionId);
        if (!sessionOld.isPresent()) {
            return Optional.empty();
        }

        sessionOld.ifPresent(s -> {
            s.schedule = session.schedule;
            s.speakers.clear();
            s.speakers.addAll(session.speakers);
            repository.persist(s);
        });
        return Optional.ofNullable(session);
    }

    public Optional<Session> findById(String sessionId) {
        Optional<Session> sessionOptional = repository.find("id", sessionId)
                .stream()
                .findFirst();
        if (sessionIntegration && sessionOptional.isPresent()) {
            Session session = sessionOptional.get();
            List<SpeakerFromService> speakerFromServices = speakerService.listAll();
            for (Speaker speaker : session.speakers) {
                Optional<SpeakerFromService> first = speakerFromServices.stream().filter(s -> s.getUuid().equals(speaker.uuid)).findFirst();
                if (first.isPresent()) {
                    SpeakerFromService speakerFromService = first.get();
                    speaker.name = speakerFromService.getNameFirst() + " " + speakerFromService.getNameLast();
                }
            }
            return Optional.of(session);
        }
        return sessionOptional;
    }

    @Transactional
    public Optional<Session> deleteById(String sessionId) {
        Optional<Session> session = findById(sessionId);
        if (!session.isPresent()) {
            return Optional.empty();
        }
        repository.delete(session.get());
        return session;
    }

}
