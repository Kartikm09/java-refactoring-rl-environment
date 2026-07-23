PYTHON ?= python3
TASK ?= task-001
PATCH ?= tasks/$(TASK)/golden/solution.patch
REPORT ?= reports/evaluations/$(TASK)/manual

.PHONY: setup format lint test integration-test coverage benchmark evaluator-test evaluate verify-evaluations security-scan verify-all
setup:
	./gradlew --version
	$(PYTHON) --version
format:
	./gradlew checkstyleMain checkstyleTest
lint:
	./gradlew checkstyleMain checkstyleTest spotbugsMain spotbugsTest
test:
	./gradlew test
integration-test:
	./gradlew integrationTest
coverage:
	./gradlew jacocoTestReport
benchmark:
	./gradlew jmh
evaluator-test:
	PYTHONPATH=evaluator/src $(PYTHON) -m unittest discover -s evaluator/tests -v
evaluate:
	PYTHONPATH=evaluator/src $(PYTHON) -m rl_evaluator.cli evaluate --repo-root . --task $(TASK) --patch $(PATCH) --output $(REPORT)
verify-evaluations:
	$(PYTHON) scripts/verify_evaluations.py
security-scan:
	$(PYTHON) scripts/secret_scan.py
verify-all: format lint test integration-test coverage evaluator-test verify-evaluations security-scan
	$(PYTHON) scripts/validate_repository.py
