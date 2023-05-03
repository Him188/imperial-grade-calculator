package me.him188.ic.grade.common

object Computing {
    val Year2 = buildAcademicYear {
        module("Algorithm Design and Analysis", 5.ects) {
            coursework("CW1", 100)
            coursework("CW2", 100)
            exam()
        }
        module("Software Engineering Design", 5.ects) {
            coursework("Test Driven Development", 20)
            coursework("Mock Objects", 20)
            coursework("Re-use and Extensibility", 20)
            coursework("Creation and Dependency", 20)
            coursework("Concurrency", 20)
            coursework("Interactive Applications", 20)
            coursework("System Integration", 20)
            coursework("Sample Exam Question", 20)
            coursework("Reflection and Rotation", 10)
            exam()
        }
        module("Models of Computation", 5.ects) {
            coursework("Models of Computation -- part I", 55)
            coursework("Models of Computation -- part II", 100)
            exam()
        }
        module("Operating Systems", 5.ects) {
            coursework("Part A mark for Pintos Task 0", 100)
            coursework("Design Document mark for Pintos Task 1", 100)
            coursework("Design Document mark for Pintos Task 2", 100)
            coursework("Design Document mark for Pintos Task 3", 100)
            exam()
        }
        module("Networks and Communications", 5.ects) {
            coursework("The Coursework", 100)
            exam()
        }
        module("Compilers", 5.ects) {
            coursework("WACC Language Specification", 100)
            coursework("Haskell Functions", 20)
            exam()
        }
        module("Probability and Statistics", 5.ects) {
            coursework("Probability 2", 40)
            coursework("Probability 3", 20)
            coursework("Probability 4", 20)
            coursework("Probability 5", 20)
            coursework("Statistics_1", 20)
            coursework("Statistics_2", 20)
            coursework("Statistics_3", 20)
            exam()
        }
        module("Symbolic Reasoning", 5.ects) {
            coursework("Coursework 1 - SAT and SMT", 20)
            coursework("Coursework 2 - LP and ASP", 20)
            exam()
        }

        module("Computing Practical 2", 15.ects) {
            submodule("Laboratory 2", 61.percent) {
                coursework("Pintos Task 0 - Alarm Clock", 100, share = 7.percent)
                coursework("Pintos Task 1 - Scheduling", 100, share = 14.percent)
                coursework("Pintos Task 2 - User Programs", 100, share = 21.percent)
                coursework("Linking and Loading", 100, share = 3.percent)
                coursework("DevOps - Continuous Delivery", 100, share = 5.percent)
                coursework("WACC Front-End", 100, share = 25.percent)
                coursework("WACC Back-End", 100, share = 25.percent)
            }
            submodule("Introduction to Prolog", 6.percent) {
                coursework("Prolog CW", 35, share = 100.percent)
            }
            submodule("Advanced Laboratory 2", 33.percent) {
                coursework("Pintos Task 3 - Virtual Memory", 100, share = 50.percent)
                coursework("True Concurrency Experiments", 100, share = 27.percent)
                coursework("WACC Extensions", 100, share = 23.percent)
            }
        }

        module("2nd Year Computing Group Project", 5.ects) {
            submodule("Designing for Real People (DRP) Project", 100.percent) {
                // TODO: 2023/5/2 update max 
                coursework("Project Milestones 1", 100, share = 5.percent)
                coursework("Project Milestones 2", 100, share = 10.percent)
                coursework("Project Milestones 3", 100, share = 10.percent)
                coursework("Project Milestones 4", 100, share = 5.percent)
                coursework("Project Documentation", 100, share = 10.percent)
                coursework("Project Presentation/Demonstration", 100, share = 50.percent)
                coursework("Law Case Study", 100, share = 10.percent)
            }
        }
    }

}