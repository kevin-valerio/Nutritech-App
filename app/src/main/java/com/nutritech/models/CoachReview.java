package com.nutritech.models;

import java.util.Calendar;

public class CoachReview {
    private User user;
    private int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    public CoachReview(User user) {
        this.user = user;
    }


    public String getReview() {
        String review = null;

        switch (user.getGoal()) {
            case PRISE_DE_MASSE:
                review = getPriseMasseReview();
                break;

            case PERTE_DE_MASSE:
                review = getPerteMasseReview();
                break;

            case MAINTIEN_DE_SON_POIDS:
                review = getMaintienPoidsReview();
                break;

        }
        return review;
    }


    private String getPerteMasseReview() {
        String review = "";

        if (user.getCalendarFoodList().get(today).getCarbs() > percentageOf(75, user.getObjGlucides())) {
            review += "Vous devez moins manger ! Vous avez actuellement mangé plus de 75% de votre objectif. Nous vous recommandons, afin de faciliter la perte de masse" +
                    ", d'ingérer de plus petites sources de glucides et donc de privilégier les légumes et les fruits qui sont fondamentaux.           \n";
        }

        if (user.getCalendarFoodList().get(today).getCalorie() > percentageOf(80, user.getKcalObj())) {
            review += "Vous avez consommé plus de 80% de calories prévues. Vous devriez ingérer moins de calories dans le cadre d'une perte de masse.           \n";
        }

        if (user.getCalendarFoodList().get(today).getProteins() > percentageOf(75, user.getObjProteines())) {
            review += "Vous avez mangé trop de protéines ! Vous n'avez pas besoin de toutes ces protéines. Peut-être devriez-vous limiter votre consommation de viande \n";
        }

        if (user.getCalendarFoodList().get(today).getLipid() > percentageOf(90, user.getObjLipides())) {
            review += "Vous avez consommé plus de 90% des lipides recommandés par notre calculateur d'objectif." +
                    " Méfiez-vous des lipides, ils peuvent être partout !       \n";
        }

        return review;
    }


    private String getMaintienPoidsReview() {
        String review = "";

        if (user.getCalendarFoodList().get(today).getCarbs() != percentageOf(100, user.getObjGlucides())) {
            review = "Vous n'êtes pas en équilibre sur vos apports glucidiques ... Peut-être devriez-vous consulter l'application plus régulierement ?     \n";
        }

        if (user.getCalendarFoodList().get(today).getCalorie() != percentageOf(100, user.getKcalObj())) {
            review = "Vous n'êtes pas en équilibre sur vos apports caloriques ... Peut-être devriez-vous consulter l'application plus régulierement ?          \n";
        }

        if (user.getCalendarFoodList().get(today).getProteins() != percentageOf(75, user.getObjProteines())) {
            review += "Vous n'êtes pas en équilibre sur vos apports protéiques ... Peut-être devriez-vous consulter l'application plus régulierement ? \n";
        }

        if (user.getCalendarFoodList().get(today).getLipid() != percentageOf(100, user.getObjLipides())) {
            review = "Vous n'êtes pas en équilibre sur vos apports lipidiques ... Peut-être devriez-vous consulter l'application plus régulierement ?           \n";
        }

        return review;
    }

    private String getPriseMasseReview() {

        String review = "";

        if (user.getCalendarFoodList().get(today).getCarbs() < percentageOf(75, user.getObjGlucides())) {
            review += "Vous devez plus manger ! Vous avez actuellement mangé moins de 75% de votre objectif. Nous vous recommandons, afin de faciliter la prise de masse" +
                    ", d'ingérer de plus grandes sources de glucides tel que le riz, les pates... sans oublier les légumes et les fruits qui sont fondamentaux.           \n";
        }

        if (user.getCalendarFoodList().get(today).getProteins() < percentageOf(75, user.getObjProteines())) {
            review += "Vous n'avez pas mangé assez de protéines aujourd'hui. Le poisson et la viande (blanche si possible) sont de très bonne sources de protéines. Il serait judicieux d'en ajouter" +
                    "à vos repas ! \n";
        }

        if (user.getCalendarFoodList().get(today).getCalorie() < percentageOf(80, user.getKcalObj())) {
            review += "Vous avez consommé moins de 80% de calories prévues. Vous devriez ingérer plus          \n";
        }

        if (user.getCalendarFoodList().get(today).getLipid() < percentageOf(90, user.getObjLipides())) {
            review += "Vous avez consommé moins de 90% des lipides recommandés par notre calculateur d'objectif. Les huiles, nottament l'huile d'olive, sont d'excellentes sources de lipides." +
                    "Peut-être devriez-vous en ajouter dans vos plats ?          \n";
        }

        return review;
    }

    private double percentageOf(double percent, double of) {
        return (percent / 100) * of;
    }
}