import sys
from evaluate import evaluate
sys.argv[1] = sys.argv[1].replace("\\", "/")
evaluate(sys.argv[1])
