package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.MessageType
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

object ScenarioSeedData {

    private val remoteDataEntryOffer = Scenario(
        id = "job_easy_01",
        title = "Remote Data Entry Offer",
        category = ScamCategory.JOB,
        difficulty = Difficulty.EASY,
        messageType = MessageType.CHAT,
        sender = "Recruitment Manager",
        messageBody = """
            Congratulations! You have been selected for a remote data-entry 
            position paying $45 per hour. Pay a $60 equipment registration 
            fee today to secure your place.
        """.trimIndent(),
        correctRiskLevel = RiskLevel.HIGH_RISK,
        warningSigns = listOf(
            WarningSign(
                id = "job_easy_01_warning_01",
                description = "The employer requests an upfront fee.",
                isActualWarning = true
            ),
            WarningSign(
                id = "job_easy_01_warning_02",
                description = "The salary appears unusually high for the role.",
                isActualWarning = true
            ),
            WarningSign(
                id = "job_easy_01_warning_03",
                description = "The message creates pressure to act immediately.",
                isActualWarning = true
            ),
            WarningSign(
                id = "job_easy_01_warning_04",
                description = "The role is listed on an official careers website.",
                isActualWarning = false
            )
        ),
        actionOptions = listOf(
            ActionOption(
                id = "job_easy_01_action_01",
                description = "Pay the fee before the offer expires.",
                isSafeAction = false,
                feedback = "Legitimate employers should not require an upfront fee to secure a role."
            ),
            ActionOption(
                id = "job_easy_01_action_02",
                description = "Send identity documents to prove eligibility.",
                isSafeAction = false,
                feedback = "Do not provide identity documents before verifying the employer."
            ),
            ActionOption(
                id = "job_easy_01_action_03",
                description = "Verify the role through the organisation's official careers page.",
                isSafeAction = true,
                feedback = "Independent verification through an official source is the safest response."
            ),
            ActionOption(
                id = "job_easy_01_action_04",
                description = "Reply and ask whether the fee can be reduced.",
                isSafeAction = false,
                feedback = "Negotiating the fee does not address the underlying scam warning signs."
            )
        ),
        explanation = """
            The offer combines an unusually high salary, an upfront payment 
            request, and urgent pressure. These are common job-scam warning signs.
        """.trimIndent(),
        verificationAdvice = """
            Search for the organisation independently and confirm the role 
            through its official careers page or verified contact details.
        """.trimIndent()
    )

    private val universityResearchSurvey = Scenario(
        id = "phishing_medium_01",
        title = "University Research Survey",
        category = ScamCategory.PHISHING,
        difficulty = Difficulty.MEDIUM,
        messageType = MessageType.EMAIL,
        sender = "research-study@students-university.org",
        subject = "Student wellbeing survey invitation",
        messageBody = """
            You are invited to complete a student wellbeing survey. Participants 
            will receive a $15 voucher. Enter your student ID and open the survey 
            using the link below.
        """.trimIndent(),
        correctRiskLevel = RiskLevel.NEEDS_VERIFICATION,
        warningSigns = listOf(
            WarningSign(
                id = "phishing_medium_01_warning_01",
                description = "The sender uses an external domain.",
                isActualWarning = true
            ),
            WarningSign(
                id = "phishing_medium_01_warning_02",
                description = "The survey requests a student ID.",
                isActualWarning = true
            ),
            WarningSign(
                id = "phishing_medium_01_warning_03",
                description = "The message offers an incentive.",
                isActualWarning = false
            ),
            WarningSign(
                id = "phishing_medium_01_warning_04",
                description = "The message has a clear subject line.",
                isActualWarning = false
            )
        ),
        actionOptions = listOf(
            ActionOption(
                id = "phishing_medium_01_action_01",
                description = "Open the link because the reward is small.",
                isSafeAction = false,
                feedback = "The size of the reward does not prove that the survey is legitimate."
            ),
            ActionOption(
                id = "phishing_medium_01_action_02",
                description = "Verify the researcher through the official university directory.",
                isSafeAction = true,
                feedback = "Independent verification can confirm whether the study is genuine."
            ),
            ActionOption(
                id = "phishing_medium_01_action_03",
                description = "Reply with the student ID before opening the survey.",
                isSafeAction = false,
                feedback = "Personal information should not be provided before verification."
            ),
            ActionOption(
                id = "phishing_medium_01_action_04",
                description = "Forward the link to classmates and ask them to test it.",
                isSafeAction = false,
                feedback = "Potentially unsafe links should not be distributed to other people."
            )
        ),
        explanation = """
            Research surveys and incentives can be legitimate, but the external 
            domain and request for a student ID require independent verification.
        """.trimIndent(),
        verificationAdvice = """
            Search the university staff directory or research portal and confirm 
            the study using official contact information.
        """.trimIndent()
    )

    private val libraryRenewalReminder = Scenario(
        id = "phishing_hard_01",
        title = "Library Renewal Reminder",
        category = ScamCategory.PHISHING,
        difficulty = Difficulty.HARD,
        messageType = MessageType.EMAIL,
        sender = "library@university.edu",
        subject = "Library loan due tomorrow",
        messageBody = """
            One of your library loans is due tomorrow. Open the official library 
            application or visit your library account to renew the item. This 
            message does not request a password or payment.
        """.trimIndent(),
        correctRiskLevel = RiskLevel.NO_CLEAR_THREAT,
        warningSigns = listOf(
            WarningSign(
                id = "phishing_hard_01_warning_01",
                description = "The message requests an immediate bank transfer.",
                isActualWarning = false
            ),
            WarningSign(
                id = "phishing_hard_01_warning_02",
                description = "The sender asks for a password.",
                isActualWarning = false
            ),
            WarningSign(
                id = "phishing_hard_01_warning_03",
                description = "The message contains a shortened external link.",
                isActualWarning = false
            ),
            WarningSign(
                id = "phishing_hard_01_warning_04",
                description = "The sender domain matches the university domain.",
                isActualWarning = false
            )
        ),
        actionOptions = listOf(
            ActionOption(
                id = "phishing_hard_01_action_01",
                description = "Reply with the university account password.",
                isSafeAction = false,
                feedback = "Passwords should never be sent by email."
            ),
            ActionOption(
                id = "phishing_hard_01_action_02",
                description = "Ignore all future library messages.",
                isSafeAction = false,
                feedback = "Automatically distrusting every message may cause genuine notices to be missed."
            ),
            ActionOption(
                id = "phishing_hard_01_action_03",
                description = "Open the official library application and check the account.",
                isSafeAction = true,
                feedback = "Using an official application is a safe way to confirm the reminder."
            ),
            ActionOption(
                id = "phishing_hard_01_action_04",
                description = "Forward the email to an unknown online forum.",
                isSafeAction = false,
                feedback = "Account-related messages should not be shared with unknown people."
            )
        ),
        explanation = """
            The message uses an official university domain, does not request 
            sensitive information, and directs the user to an existing official service.
        """.trimIndent(),
        verificationAdvice = """
            Open the official library application or type the university library 
            address manually instead of relying on forwarded links.
        """.trimIndent()
    )

    val scenarios: List<Scenario> = listOf(
        remoteDataEntryOffer,
        universityResearchSurvey,
        libraryRenewalReminder
    )

}