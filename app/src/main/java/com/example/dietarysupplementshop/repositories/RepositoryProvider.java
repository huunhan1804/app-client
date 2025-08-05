package com.example.dietarysupplementshop.repositories;

public class RepositoryProvider {

    private static final boolean USE_FAKE_REPOSITORIES = false;

    public static IAgencyRepository getAgencyRepository() {
        if (USE_FAKE_REPOSITORIES) {
            return null;
        } else {
            return AgencyRegistrationRepository.getInstance();
        }
    }
}