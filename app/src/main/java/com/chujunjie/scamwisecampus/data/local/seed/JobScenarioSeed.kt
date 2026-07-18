package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.MessageType
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

internal object JobScenarioSeed {

    val scenarios: List<Scenario> = listOf(
        Scenario(
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
        ),
        Scenario(
            id = "job_medium_01",
            title = "Campus Ambassador Application",
            category = ScamCategory.JOB,
            difficulty = Difficulty.MEDIUM,
            messageType = MessageType.EMAIL,
            sender = "campus-program@brand-partners.example",
            subject = "Campus ambassador opportunity",
            messageBody = """
                We are recruiting university students as campus ambassadors.
                Please submit your resume and student card number through our
                application form by Friday. Selected students receive training
                and a monthly allowance.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NEEDS_VERIFICATION,
            warningSigns = listOf(
                WarningSign(
                    id = "job_medium_01_warning_01",
                    description = "The sender uses an external organisation domain.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "job_medium_01_warning_02",
                    description = "The application requests a student card number.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "job_medium_01_warning_03",
                    description = "The message includes a clear application deadline.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "job_medium_01_warning_04",
                    description = "The role includes training and an allowance.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "job_medium_01_action_01",
                    description = "Submit the student card number immediately.",
                    isSafeAction = false,
                    feedback = "Personal identifiers should not be submitted before the organisation is verified."
                ),
                ActionOption(
                    id = "job_medium_01_action_02",
                    description = "Confirm the programme through the brand's official website or university careers service.",
                    isSafeAction = true,
                    feedback = "Independent verification can confirm whether the opportunity is genuine."
                ),
                ActionOption(
                    id = "job_medium_01_action_03",
                    description = "Forward the form to classmates before checking it.",
                    isSafeAction = false,
                    feedback = "Unverified forms should not be distributed to other students."
                ),
                ActionOption(
                    id = "job_medium_01_action_04",
                    description = "Upload a passport instead of a student card.",
                    isSafeAction = false,
                    feedback = "Providing a more sensitive document increases the privacy risk."
                )
            ),
            explanation = """
                Campus ambassador programmes can be legitimate, but an external
                sender and a request for a student identifier require verification.
            """.trimIndent(),
            verificationAdvice = """
                Search for the programme on the organisation's official website
                and confirm it with the university careers service before applying.
            """.trimIndent()
        ),
        Scenario(
            id = "job_hard_01",
            title = "Verified Internship Interview",
            category = ScamCategory.JOB,
            difficulty = Difficulty.HARD,
            messageType = MessageType.EMAIL,
            sender = "talent@verifiedcompany.example",
            subject = "Interview confirmation for software internship",
            messageBody = """
                Thank you for applying through our official careers portal.
                Your online interview is scheduled for Tuesday at 10:00 a.m.
                The meeting invitation is also available in your applicant account.
                No payment or identity document is requested in this email.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NO_CLEAR_THREAT,
            warningSigns = listOf(
                WarningSign(
                    id = "job_hard_01_warning_01",
                    description = "The message requests an upfront training payment.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "job_hard_01_warning_02",
                    description = "The sender asks for an account password.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "job_hard_01_warning_03",
                    description = "The interview can be confirmed through the existing applicant account.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "job_hard_01_warning_04",
                    description = "The message threatens to cancel the application within minutes.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "job_hard_01_action_01",
                    description = "Confirm the interview through the existing applicant account.",
                    isSafeAction = true,
                    feedback = "Using the official applicant portal provides an independent confirmation path."
                ),
                ActionOption(
                    id = "job_hard_01_action_02",
                    description = "Send a passport copy even though it was not requested.",
                    isSafeAction = false,
                    feedback = "Do not provide unnecessary identity documents."
                ),
                ActionOption(
                    id = "job_hard_01_action_03",
                    description = "Assume every interview email is fraudulent.",
                    isSafeAction = false,
                    feedback = "Treating all recruitment messages as scams may cause genuine opportunities to be missed."
                ),
                ActionOption(
                    id = "job_hard_01_action_04",
                    description = "Share the interview link publicly.",
                    isSafeAction = false,
                    feedback = "Private meeting details should not be shared publicly."
                )
            ),
            explanation = """
                The email refers to an application the student already made,
                requests no payment or sensitive information, and provides an
                independent confirmation path through the official applicant portal.
            """.trimIndent(),
            verificationAdvice = """
                Sign in to the applicant account using a saved bookmark or manually
                typed official address and confirm the interview details there.
            """.trimIndent()
        )
    )
}
