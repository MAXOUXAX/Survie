package com.lyorine.survie.utils;

public enum Reference {

    INVSURVIE("§7» §a§lSurvie"),
    INVSURVIE_SPAWN("§7» §7Retourner au §aspawn"),
    INVSURVIE_SPAWNLORE("§7Cliquez ici pour retourner au §aspawn §7du serveur"),
    INVSURVIE_INFO("§7» §7Obtenir des §cinformations"),
    INVSURVIE_INFOLORE("§7Cliquez ici pour obtenir des §cinformations §7sur le serveur"),
    INVSURVIE_BOUTIQUE("§7» §e§mBoutique"),
    INVSURVIE_BOUTIQUELORE("§c§lINDISPONIBLE"),
    INVSURVIE_EXP("§7» §bTransformer son expérience"),
    INVSURVIE_EXPLORE("§7Cliquez ici pour transformer votre", "§7expérience en bouteille d'expérience !"),
    INVSURVIE_RTP("§7» §aTéléportation aléatoire"),
    INVSURVIE_RTPLORE("§7Cliquez ici pour vous téléporter aléatoirement", "§7sur la carte !"),
    INVSURVIE_END("§7» §aTéléportation dans §el'end"),
    INVSURVIE_ENDLORE("§7Cliquez ici pour vous téléporter dans §el'end"),

    INVHOME("§7» §6Homes"),
    INVHOME_HOME("§7» §6"),
    INVHOME_MANAGE("§7» §cGérer mes homes"),
    INVHOME_MANAGELORE("§7Cliquez ici afin d'ouvrir le gestionnaire de homes !"),

    INVMHOME("§7» §cGestion des §6homes"),
    INVMHOME_CREATE("§7» §aCréer un §6home"),
    INVMHOME_CREATELORE("§7Cliquez ici pour créer un §6home §7à votre emplacement actuel !"),
    INVMHOME_RENAME("§7» §eRenommer un §6home"),
    INVMHOME_RENAMELORE("§7Cliquez ici pour renommer un de vos §6homes§7 existant !"),
    INVMHOME_DELETE("§7» §cSupprimer un §6home"),
    INVMHOME_DELETELORE("§7Cliquez ici pour supprimer un de vos §6homes", "§c§lATTENTION, ACTION IRRÉVERSIBLE !"),
    INVMHOME_CHANGE("§7» §eModifier le matériel d'un §6home"),
    INVMHOME_CHANGELORE("§7Cliquez ici pour modifier le matériel d'un de vos §6homes"),
    INVMHOME_SLOT("§7» §eModifier le slot d'un §6home"),
    INVMHOME_SLOTLORE("§7Cliquez ici pour modifier le slot", "§7d'un de vos §6homes §7dans l'inventaire"),

    INVDELETEHOME("§7» §cSupprimer un §6home"),
    INVRENAMEHOME("§7» §eRenommer un §6home"),
    INVCHANGEHOME("§7» §eMatériel d'un §6home"),
    INVSLOTHOME("§7» §eSlot d'un §6home"),

    INVCONFIRMDELETE("§7» §cÊtes-vous sûr ? §6"),
    INVCONFIRMDELETE_STRIP("» Êtes-vous sûr ? "),
    INVMATERIALCHANGE("§7» §aQuel matériel ? §6"),
    INVMATERIALCHANGE_STRIP("» Quel matériel ? "),
    INV_CONFIRM("§a§lConfirmer ✔"),
    INV_CANCEL("§c§lAnnuler ✘"),

    INVEXP("§7» §e§lExpérience"),
    INVEXP_PLUS1("§7» §a+1"),
    INVEXP_PLUS1LORE("§7Cliquez ici pour ajouter 1xp"),
    INVEXP_PLUS5("§7» §a+5"),
    INVEXP_PLUS5LORE("§7Cliquez ici pour ajouter 5xp"),
    INVEXP_PLUS10("§7» §a+10"),
    INVEXP_PLUS10LORE("§7Cliquez ici pour ajouter 10xp"),
    INVEXP_PLUS50("§7» §a+50"),
    INVEXP_PLUS50LORE("§7Cliquez ici pour ajouter 50xp"),
    INVEXP_PLUS100("§7» §a+100"),
    INVEXP_PLUS100LORE("§7Cliquez ici pour ajouter 100xp"),
    INVEXP_MAX("§7» §a§lPrendre le maximum"),
    INVEXP_MAXLORE("§7Cliquez ici pour récupérer toute votre xp"),
    INVEXP_LESS1("§7» §c-1"),
    INVEXP_LESS1LORE("§7Cliquez ici pour retirer 1xp"),
    INVEXP_LESS5("§7» §c-5"),
    INVEXP_LESS5LORE("§7Cliquez ici pour retirer 5xp"),
    INVEXP_LESS10("§7» §c-10"),
    INVEXP_LESS10LORE("§7Cliquez ici pour retirer 10xp"),
    INVEXP_LESS50("§7» §c-50"),
    INVEXP_LESS50LORE("§7Cliquez ici pour retirer 50xp"),
    INVEXP_LESS100("§7» §c-100"),
    INVEXP_LESS100LORE("§7Cliquez ici pour retirer 100xp"),

    INVBOUTIQUE_ACHAT("§a§l» Acheter"),
    INVBOUTIQUE_ACHATLORE("§7Cliquez ici pour ouvrir le menu d'achat"),
    INVBOUTIQUE_VENTE("§c§l» Vendre"),
    INVBOUTIQUE_VENTELORE("§7Cliquez ici pour ouvrir le menu de vente"),

    INVRTP("§7» §a§lTéléportation aléatoire"),

    INVRAID("§7» §c§lRAID"),
    INVRAID_DIFFICULTY("§7» §e§lDifficulté§r§f: "),
    INVRAID_VAGUECOUNT("§7» §9§lNombre de vague§r§f: "),
    INVRAID_DIFFICULTYLORE("§7Permet de régler le niveau de difficulté","du raid ! §a§lFACILE§r§f, §6§lMOYEN§r§f, §c§lHARDCORE§r§f, §0§lEXTREME"),
    INVRAID_VAGUECOUNTLORE("§7Permet de régler le nombre de vague"),
    INVRAID_START("§a§lDémarrer"),
    INVRAID_STARTLORE("§7Permet de démarrer le RAID !"),

    TIMBERAXE_NAME("§e§lTimber§6§lAxe"),
    TIMBERAXE_LORE("§7Cette hache détruit tout l'arbre d'un coup, c'est du maraboutage !"),
    ;


    private String name;
    private String[] names;

    public String getName() {
        return name;
    }

    Reference(String name) {
        this.name = name;
    }

    Reference(String... names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

}
