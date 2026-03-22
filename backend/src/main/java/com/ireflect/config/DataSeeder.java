package com.ireflect.config;

import com.ireflect.document.CategoryDocument;
import com.ireflect.document.CategoryDocument.PromptItem;
import com.ireflect.document.CategoryDocument.PromptStages;
import com.ireflect.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataSeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) return; // Only seed if empty

        categoryRepository.saveAll(List.of(
            buildCategory("deep-meaning-thoughts", "Deep meaning thoughts",
                "A grounded space for purpose and inner truth.",
                false,
                List.of(
                    new PromptItem("dm-o1", "What truth have you been quietly carrying?", "deep"),
                    new PromptItem("dm-o2", "What part of yourself have you been neglecting lately?", "gentle")
                ),
                List.of(
                    new PromptItem("dm-d1", "What kind of life feels most honest to you?", "deep"),
                    new PromptItem("dm-d2", "What belief is making this heavier than it needs to be?", "gentle")
                ),
                List.of(
                    new PromptItem("dm-r1", "What would change if you trusted yourself more?", "encouraging"),
                    new PromptItem("dm-r2", "What would you tell yourself if you were being completely kind?", "gentle")
                ),
                List.of(
                    new PromptItem("dm-re1", "What are you ready to let go of today?", "calm"),
                    new PromptItem("dm-re2", "What do you want to carry forward from this reflection?", "encouraging")
                ),
                List.of(
                    new PromptItem("dm-c1", "How do you feel right now, compared to when you started?", "gentle"),
                    new PromptItem("dm-c2", "Is there one thing you'd like to remember from today?", "calm")
                )
            ),

            buildCategory("trauma-relief-thoughts", "Trauma relief thoughts",
                "Gentle encounters to release and feel lighter.",
                false,
                List.of(
                    new PromptItem("tr-o1", "What moment still lingers in your chest?", "gentle"),
                    new PromptItem("tr-o2", "What feeling has been hardest to name lately?", "gentle")
                ),
                List.of(
                    new PromptItem("tr-d1", "What would it feel like to set this down, even briefly?", "gentle"),
                    new PromptItem("tr-d2", "What part of this experience do you wish someone understood?", "deep")
                ),
                List.of(
                    new PromptItem("tr-r1", "What if this feeling is trying to protect you?", "gentle"),
                    new PromptItem("tr-r2", "What would healing look like, even just a small step?", "encouraging")
                ),
                List.of(
                    new PromptItem("tr-re1", "What comfort do you wish you could give yourself right now?", "gentle"),
                    new PromptItem("tr-re2", "What small kindness can you offer yourself today?", "calm")
                ),
                List.of(
                    new PromptItem("tr-c1", "How are you feeling after sitting with these thoughts?", "gentle"),
                    new PromptItem("tr-c2", "Is there anything you'd like to acknowledge before we close?", "calm")
                )
            ),

            buildCategory("purely-happy-thoughts", "Purely happy thoughts",
                "Gratitude and soft joy.",
                false,
                List.of(
                    new PromptItem("ph-o1", "What simple thing in life has been quietly beautiful lately?", "encouraging"),
                    new PromptItem("ph-o2", "What made you smile recently, even just a little?", "encouraging")
                ),
                List.of(
                    new PromptItem("ph-d1", "What are you uncovering gratitude in lately?", "gentle"),
                    new PromptItem("ph-d2", "Who in your life makes you feel seen?", "deep")
                ),
                List.of(
                    new PromptItem("ph-r1", "What would happen if you let yourself enjoy this more fully?", "encouraging"),
                    new PromptItem("ph-r2", "What if joy doesn't need to be earned?", "gentle")
                ),
                List.of(
                    new PromptItem("ph-re1", "What warmth do you want to hold onto from today?", "calm"),
                    new PromptItem("ph-re2", "What are you grateful for in this exact moment?", "encouraging")
                ),
                List.of(
                    new PromptItem("ph-c1", "How do you feel after spending time with these happy thoughts?", "encouraging"),
                    new PromptItem("ph-c2", "What's one joyful thing you'd like to carry into tomorrow?", "gentle")
                )
            ),

            buildCategory("exciting-thoughts", "Exciting thoughts",
                "Dreams, future possibilities, and creative exploration.",
                false,
                List.of(
                    new PromptItem("et-o1", "What possibility secretly excites you?", "encouraging"),
                    new PromptItem("et-o2", "What future version of you feels thrilling to imagine?", "encouraging")
                ),
                List.of(
                    new PromptItem("et-d1", "What scene even excites you when you picture it?", "deep"),
                    new PromptItem("et-d2", "What would you chase if fear wasn't a factor?", "direct-kind")
                ),
                List.of(
                    new PromptItem("et-r1", "What yes secretly excites you?", "encouraging"),
                    new PromptItem("et-r2", "What if your boldest idea is actually the right one?", "encouraging")
                ),
                List.of(
                    new PromptItem("et-re1", "What first step feels both exciting and possible?", "encouraging"),
                    new PromptItem("et-re2", "What energy do you want to bring into this next chapter?", "calm")
                ),
                List.of(
                    new PromptItem("et-c1", "How do you feel after exploring these exciting thoughts?", "gentle"),
                    new PromptItem("et-c2", "What's one exciting thing you're taking away from this?", "encouraging")
                )
            )
        ));

        System.out.println("✅ Seeded 4 launch categories with prompt banks");
    }

    private CategoryDocument buildCategory(String slug, String name, String description,
                                           boolean isPremium,
                                           List<PromptItem> opening,
                                           List<PromptItem> deepening,
                                           List<PromptItem> reframing,
                                           List<PromptItem> release,
                                           List<PromptItem> closure) {
        CategoryDocument cat = new CategoryDocument();
        cat.setSlug(slug);
        cat.setName(name);
        cat.setDescription(description);
        cat.setPremium(isPremium);
        cat.setActive(true);

        PromptStages stages = new PromptStages();
        stages.setOpening(opening);
        stages.setDeepening(deepening);
        stages.setReframing(reframing);
        stages.setRelease(release);
        stages.setClosure(closure);
        cat.setPromptStages(stages);

        return cat;
    }
}
